package com.back.entities.mappers;


import com.back.entities.Comment;
import com.back.entities.Task;
import com.back.entities.User;
import com.back.entities.dto.CommentResponse;
import com.back.entities.dto.TaskResponse;
import com.back.entities.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommentMapper {

    public Comment fromResponseToEntity(CommentResponse commentResponse,
                                        User user,
                                        Task task
                                        ){

        return Comment.builder()
                .id(commentResponse.getId().longValue())
                .content(commentResponse.getContent())
                .user(user)
                .task(task)
                .build();
    }

    public void editCommentContentFromEntity(String newContent,
                                             Comment comment
                                             ){
        if(newContent != null){
            comment.setContent(newContent);
        }

    }

    public CommentResponse fromEntityToResponse(Comment comment,
                                        UserResponse user
                                        //,TaskResponse task
    ){

        return CommentResponse.builder()
                .id(comment.getId().longValue())
                .content(comment.getContent())
                .user(user)
                .createdAt(comment.getCreatedAt())
                //.task(task)
                .build();
    }



}
