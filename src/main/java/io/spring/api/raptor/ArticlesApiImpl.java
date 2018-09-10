package io.spring.api.raptor;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.spring.api.exception.InvalidRequestException;
import io.spring.api.exception.NoAuthorizationException;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.ArticleQueryService;
import io.spring.application.data.ArticleData;
import io.spring.application.data.ArticleDataList;
import io.spring.context.UserContext;
import io.spring.core.article.ArticleRepository;
import io.spring.core.service.AuthorizationService;
import io.spring.core.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dozer.Mapper;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ArticlesApiImpl implements ArticlesApi {


    private ArticleQueryService articleQueryService;
    private ArticleRepository articleRepository;
    private Mapper mapper;
    private Validator validator;

    @Autowired
    public ArticlesApiImpl(ArticleQueryService articleQueryService, ArticleRepository articleRepository, Mapper mapper, Validator validator) {
        this.articleQueryService = articleQueryService;
        this.articleRepository = articleRepository;
        this.mapper = mapper;
        this.validator = validator;
    }


    @Override
    public MultipleArticlesResponse getFollowUserArticles(Page request) {
        User user = UserContext.getUser();
        ArticleDataList userFeed = articleQueryService.findUserFeed(user, new io.spring.application.Page(request.getOffset(), request.getLimit()));
        return mapper.map(userFeed, MultipleArticlesResponse.class);
    }

    @Override
    public MultipleArticlesResponse getArticles(ArticleRequest request) {
        ArticleDataList recentArticles = articleQueryService.findRecentArticles(request.getTag(), request.getAuthor(), request.getFavorited(), new io.spring.application.Page(request.getOffset(), request.getLimit()), UserContext.getUser());
        return mapper.map(recentArticles, MultipleArticlesResponse.class);
    }

    @Override
    public SingleArticleResponse createArticle(CreateArticleRequest request) {
        User user = UserContext.getUser();
        NewArticleParam newArticleParam = mapper.map(request.getArticle(), NewArticleParam.class);
        if (validator.supports(NewArticleParam.class)) {
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(newArticleParam, "newArticleParam");
            validator.validate(newArticleParam,bindingResult);

            if(bindingResult.hasErrors()){
                throw new InvalidRequestException(bindingResult);
            }
        }

        io.spring.core.article.Article article = new io.spring.core.article.Article(
                newArticleParam.getTitle(),
                newArticleParam.getDescription(),
                newArticleParam.getBody(),
                newArticleParam.getTagList(),
                user.getId());
        articleRepository.save(article);
        final ArticleData articleData = articleQueryService.findById(article.getId(), user).get();
        Article article1 = mapper.map(articleData, Article.class);

        SingleArticleResponse singleArticleResponse = new SingleArticleResponse();
        singleArticleResponse.setArticle(article1);
        return singleArticleResponse;
    }

    @Override
    public SingleArticleResponse getArticle(SingleArticleRequest request) {
        User user = UserContext.getUser();
        String slug = request.getSlug();
        ArticleData articleData1 = articleQueryService.findBySlug(slug, user).orElseThrow(ResourceNotFoundException::new);
        Article map = mapper.map(articleData1, Article.class);
        return new SingleArticleResponse(map);
    }

    @Override
    public SingleArticleResponse updateArticle(EditArticleRequest request) {
        User user = UserContext.getUser();
        String slug = request.getSlug();
        ArticleData articleData = articleRepository.findBySlug(slug).map(article -> {
            if (!AuthorizationService.canWriteArticle(user, article)) {
                throw new NoAuthorizationException();
            }
            UpdateArticleRequest article1 = request.getArticle();
            article.update(
                    article1.getTitle(),
                    article1.getDescription(),
                    article1.getBody());
            articleRepository.save(article);
            return articleQueryService.findBySlug(slug, user).get();
        }).orElseThrow(ResourceNotFoundException::new);

        Article article = mapper.map(articleData, Article.class);
        return new SingleArticleResponse(article);
    }

    @Override
    public BaseResponse deleteArticle(SingleArticleRequest request) {
        User user = UserContext.getUser();
        String slug = request.getSlug();

        return articleRepository.findBySlug(slug).map(article -> {
            if (!AuthorizationService.canWriteArticle(user, article)) {
                throw new NoAuthorizationException();
            }
            articleRepository.remove(article);
            return new BaseResponse();
        }).orElseThrow(ResourceNotFoundException::new);

    }

    @Getter
    @JsonRootName("article")
    @Setter
    @NoArgsConstructor
    public static class NewArticleParam {
        @NotBlank(message = "can't be empty")
        private String title;
        @NotBlank(message = "can't be empty")
        private String description;
        @NotBlank(message = "can't be empty")
        private String body;
        private String[] tagList;
    }
}

