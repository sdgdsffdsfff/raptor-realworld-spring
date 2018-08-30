package io.spring.api.raptor;

import io.spring.application.ArticleQueryService;
import io.spring.application.data.ArticleDataList;
import io.spring.context.UserContext;
import io.spring.core.article.ArticleRepository;
import io.spring.core.user.User;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
        return null;
    }

    @Override
    public SingleArticleResponse getArticle(SingleArticleRequest request) {
        return null;
    }

    @Override
    public SingleArticleResponse updateArticle(EditArticleRequest request) {
        return null;
    }

    @Override
    public BaseResponse deleteArticle(SingleArticleRequest request) {
        return null;
    }
}
