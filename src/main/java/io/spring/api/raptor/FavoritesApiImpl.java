package io.spring.api.raptor;

import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.ArticleQueryService;
import io.spring.context.UserContext;
import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.favorite.ArticleFavorite;
import io.spring.core.favorite.ArticleFavoriteRepository;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoritesApiImpl implements FavoritesApi {
    private ArticleFavoriteRepository articleFavoriteRepository;
    private ArticleRepository articleRepository;
    private ArticleQueryService articleQueryService;
    private Mapper mapper;

    @Autowired
    public FavoritesApiImpl(ArticleFavoriteRepository articleFavoriteRepository,
                            ArticleRepository articleRepository,
                            ArticleQueryService articleQueryService,
                            Mapper mapper) {
        this.articleFavoriteRepository = articleFavoriteRepository;
        this.articleRepository = articleRepository;
        this.articleQueryService = articleQueryService;
        this.mapper = mapper;
    }

    @Override
    public SingleArticleResponse favoriteArticle(FavoriteRequest request) {
        io.spring.core.user.User user = UserContext.getUser();
        Article article = getArticle(request.getSlug());
        ArticleFavorite articleFavorite = new ArticleFavorite(article.getId(), user.getId());
        articleFavoriteRepository.save(articleFavorite);
        return new SingleArticleResponse(mapper.map(articleQueryService.findBySlug(request.getSlug(), user).get(), io.spring.api.raptor.Article.class));
    }

    @Override
    public SingleArticleResponse unFavoriteArticle(FavoriteRequest request) {
        io.spring.core.user.User user = UserContext.getUser();
        Article article = getArticle(request.getSlug());
        articleFavoriteRepository.find(article.getId(), user.getId()).ifPresent(favorite -> {
            articleFavoriteRepository.remove(favorite);
        });
        return new SingleArticleResponse(mapper.map(articleQueryService.findBySlug(request.getSlug(), user).get(), io.spring.api.raptor.Article.class));
    }

    private Article getArticle(String slug) {
        return articleRepository.findBySlug(slug).map(article -> article)
                .orElseThrow(ResourceNotFoundException::new);
    }
}
