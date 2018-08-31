package io.spring.api.raptor;

import io.spring.api.exception.NoAuthorizationException;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.CommentQueryService;
import io.spring.application.data.CommentData;
import io.spring.context.UserContext;
import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.comment.CommentRepository;
import io.spring.core.service.AuthorizationService;
import io.spring.core.user.User;
import io.spring.infrastructure.mybatis.readservice.CommentReadService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CommentsApiImpl implements CommentsApi {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentReadService commentReadService;

    @Autowired
    private CommentQueryService commentQueryService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private Mapper mapper;

    @Override
    public CommentsResponse getComments(CommnetsRequest request) {
        String slug = request.getSlug();
        Article article = findArticle(slug);

        List<CommentData> comments = commentReadService.findByArticleId(article.getId());

        List<Comment> commentList = comments.stream().map(item -> mapper.map(item, Comment.class)).collect(Collectors.toList());

        CommentsResponse commentsResponse = new CommentsResponse();
        commentsResponse.setComments(commentList);

        return commentsResponse;
    }

    @Override
    public SingleCommentResponse addComment(NewCommentRequest request) {


        Article article = findArticle(request.getSlug());
/*        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }*/
        User user = UserContext.getUser();
        io.spring.core.comment.Comment comment = new io.spring.core.comment.Comment(request.getComment().getBody(), user.getId(), article.getId());
        commentRepository.save(comment);
        CommentData commentData = commentQueryService.findById(comment.getId(), user).get();
        Comment comment1 = mapper.map(commentData, Comment.class);
        return new SingleCommentResponse(comment1);
    }

    @Override
    public BaseResponse deleteComment(DeleteCommentRequest request) {
        String slug = request.getSlug();
        Article article = findArticle(slug);

        commentRepository.findById(article.getId(), request.getId() + "").map(comment -> {
            if (!AuthorizationService.canWriteComment(UserContext.getUser(), article, comment)) {
                throw new NoAuthorizationException();
            }
            commentRepository.remove(comment);
            return ResponseEntity.noContent().build();
        }).orElseThrow(ResourceNotFoundException::new);

        return new BaseResponse();
    }

    private Article findArticle(String slug) {
        return articleRepository.findBySlug(slug).map(article -> article).orElseThrow(ResourceNotFoundException::new);
    }

}
