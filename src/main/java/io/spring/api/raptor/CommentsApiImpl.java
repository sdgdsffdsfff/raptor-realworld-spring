package io.spring.api.raptor;

import org.springframework.web.bind.annotation.RestController;

//@RestController
public class CommentsApiImpl implements CommentsApi{
    @Override
    public CommentsResponse getComments(CommnetsRequest request) {
        return null;
    }

    @Override
    public SingleCommentResponse addComment(NewCommentRequest request) {
        return null;
    }

    @Override
    public BaseResponse deleteComment(DeleteCommentRequest request) {
        return null;
    }
}
