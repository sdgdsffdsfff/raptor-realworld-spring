package io.spring.api.raptor;

import io.spring.api.exception.NoAuthorizationException;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.ArticleQueryService;
import io.spring.application.data.ArticleData;
import io.spring.application.data.ArticleDataList;
import io.spring.context.UserContext;
import io.spring.core.article.ArticleRepository;
import io.spring.core.service.AuthorizationService;
import io.spring.core.user.User;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
/**
 * @Author：zhangchengxi
 * @Date：2018/9/6 19:48
 */
@RestController
public class ArticlesApiImpl implements ArticlesApi {


    private ArticleQueryService articleQueryService;
    private ArticleRepository articleRepository;
    private Mapper mapper;

    @Autowired
    public ArticlesApiImpl(ArticleQueryService articleQueryService, ArticleRepository articleRepository, Mapper mapper) {
        this.articleQueryService = articleQueryService;
        this.articleRepository = articleRepository;
        this.mapper = mapper;
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
    public SingleArticleResponse createArticle(Article request) {
        User user = UserContext.getUser();

        List<String> tagList = request.getTagList();
        if (CollectionUtils.isEmpty(tagList)) {
            tagList = new ArrayList<>();
        }
        io.spring.core.article.Article article = new io.spring.core.article.Article(
                request.getTitle(),
                request.getDescription(),
                request.getBody(),
                tagList.toArray(new String[tagList.size()]),
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
}
