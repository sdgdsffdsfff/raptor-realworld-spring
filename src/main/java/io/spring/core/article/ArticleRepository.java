package io.spring.core.article;

import java.util.Optional;
/**
 * @Author：zhangchengxi
 * @Date：2018/9/6 19:48
 */
public interface ArticleRepository {

    void save(Article article);

    Optional<Article> findById(String id);

    Optional<Article> findBySlug(String slug);


    void remove(Article article);
}
