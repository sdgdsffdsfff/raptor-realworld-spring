package io.spring.infrastructure.mybatis.mapper;

import io.spring.core.comment.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
/**
 * @Author：zhangchengxi
 * @Date：2018/9/6 19:48
 */
@Component
@Mapper
public interface CommentMapper {
    void insert(@Param("comment") Comment comment);

    Comment findById(@Param("articleId") String articleId, @Param("id") String id);

    void delete(@Param("id") String id);
}
