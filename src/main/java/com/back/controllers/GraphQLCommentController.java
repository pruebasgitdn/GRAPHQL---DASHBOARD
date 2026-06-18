package com.back.controllers;
import com.back.entities.dto.CommentResponse;
import com.back.entities.dto.CreateCommentInput;
import com.back.entities.dto.CreateProjectInput;
import com.back.entities.dto.ProjectResponse;
import com.back.security.UserDetailsImpl;
import com.back.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class GraphQLCommentController {

    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "sendComment")
    public CommentResponse sendComment(@Argument(name = "commentInput") CreateCommentInput commentInput,
                                       @AuthenticationPrincipal UserDetailsImpl authenticated

    ){
        if(authenticated == null){
            throw new AuthenticationCredentialsNotFoundException("No se econtraron las credenciales de autenticacion");
        }

        return commentService.sendComment(commentInput,authenticated.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "deleteComment")
    public Boolean deleteComment(@Argument(name = "id") Long id,
                                       @AuthenticationPrincipal UserDetailsImpl authenticated

    ){
        if(authenticated == null){
            throw new AuthenticationCredentialsNotFoundException("No se econtraron las credenciales de autenticacion");
        }

        return commentService.deleteComment(id);
    }

    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "editComment")
    public CommentResponse editComment(@Argument(name = "id") Long id,
                                       @Argument(name = "newContent") String newContent,
                                 @AuthenticationPrincipal UserDetailsImpl authenticated

    ){
        if(authenticated == null){
            throw new AuthenticationCredentialsNotFoundException("No se econtraron las credenciales de autenticacion");
        }

        return commentService.editCommentContent(newContent,id);
    }



    @QueryMapping(name = "getCommentById")
    public CommentResponse getCommentById(@Argument(name = "id") Long id
    ){
        return commentService.getCommentById(id);
    }

    @QueryMapping(name = "getCommentsByTaskId")
    public List<CommentResponse> getCommentsByTaskId(@Argument(name = "taskId") Long id
    ){
        return commentService.getCommentsByTaskId(id);
    }

    @QueryMapping(name = "getAllComments")
    public List<CommentResponse> getAllComments(){
        return commentService.getAllComments();
    }


}
