package io.spring.api.raptor;

import org.springframework.web.bind.annotation.RestController;

//@RestController
public class FavoritesApiImpl implements FavoritesApi {
    @Override
    public SingleArticleResponse favoriteArticle(FavoriteRequest request) {
        return null;
    }

    @Override
    public SingleArticleResponse unFavoriteArticle(FavoriteRequest request) {
        return null;
    }
}
