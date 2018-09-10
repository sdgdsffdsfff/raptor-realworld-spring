package io.spring.core.favorite;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author：zhangchengxi
 * @Date：2018/9/6 19:48
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ArticleFavorite {
    private String articleId;
    private String userId;

    public ArticleFavorite(String articleId, String userId) {
        this.articleId = articleId;
        this.userId = userId;
    }
}
