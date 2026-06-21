package com.back.services;


import com.back.entities.Comment;
import com.back.entities.dto.CommentResponse;
import com.back.entities.dto.CreateCommentInput;

import java.util.List;
import java.util.UUID;

public interface CommentService {


    CommentResponse sendComment(CreateCommentInput commentInput, UUID userId);

    CommentResponse getCommentById(Long commentId);

    List<CommentResponse> getCommentsByTaskId(Long taskId);

    List<CommentResponse> getAllComments();

    CommentResponse editCommentContent(String newContent,Long id, UUID userId);

    Boolean deleteComment(Long id);


}
