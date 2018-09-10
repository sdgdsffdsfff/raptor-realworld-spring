package io.spring.core.favorite;

import java.util.Optional;
/**
 * @Author：zhangchengxi
 * @Date：2018/9/6 19:48
 */
public interface ArticleFavoriteRepository {
    void save(ArticleFavorite articleFavorite);

    Optional<ArticleFavorite> find(String articleId, String userId);

    void remove(ArticleFavorite favorite);
}
