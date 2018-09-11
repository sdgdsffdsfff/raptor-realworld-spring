package io.spring.infrastructure.repository;

import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.article.Tag;
import io.spring.infrastructure.mybatis.mapper.ArticleMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

import java.util.Optional;

/**
 * @Author：zhangchengxi
 * @Date：2018/9/6 19:48
 */
@Repository
public class MyBatisArticleRepository implements ArticleRepository {
    private ArticleMapper articleMapper;

    public MyBatisArticleRepository(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void save(Article article) {
        if (articleMapper.findById(article.getId()) == null) {
            createNew(article);
        } else {
            articleMapper.update(article);
        }
    }

    private void createNew(Article article) {
        for (Tag tag : article.getTags()) {
            if (!articleMapper.findTag(tag.getName())) {
                tag.setId(UUID.randomUUID().toString());
                articleMapper.insertTag(tag);
            }
            articleMapper.insertArticleTagRelation(article.getId(), tag.getId());
        }
        article.setId(UUID.randomUUID().toString());
        articleMapper.insert(article);
    }

    @Override
    public Optional<Article> findById(String id) {
        return Optional.ofNullable(articleMapper.findById(id));
    }

    @Override
    public Optional<Article> findBySlug(String slug) {
        return Optional.ofNullable(articleMapper.findBySlug(slug));
    }

    @Override
    public void remove(Article article) {
        articleMapper.delete(article.getId());
    }
}
