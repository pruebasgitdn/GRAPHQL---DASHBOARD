package com.back.services.impl;


import com.back.entities.Comment;
import com.back.entities.Task;
import com.back.entities.User;
import com.back.entities.dto.CommentResponse;
import com.back.entities.dto.CreateCommentInput;
import com.back.entities.dto.TaskResponse;
import com.back.entities.dto.UserResponse;
import com.back.entities.mappers.CommentMapper;
import com.back.entities.mappers.TasksMapper;
import com.back.entities.mappers.UserMapper;
import com.back.exceptions.ItemNotFoundException;
import com.back.repositories.CommentRepository;
import com.back.repositories.TasksRepository;
import com.back.repositories.UserRepository;
import com.back.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TasksRepository tasksRepository;
    private final TasksMapper tasksMapper;
    private final CommentRepository commentRepository;
    private  final CommentMapper commentMapper;


    @Override
    @Caching(evict = {

            @CacheEvict(value = "comment", key = "#commentInput.taskId"),
            @CacheEvict(value = "commentsByTask", key = "#commentInput.taskId")
    })
    public CommentResponse sendComment(CreateCommentInput commentInput, UUID userId) {

        User user = userRepository.findById(userId).
                orElseThrow(()->{
                    throw  new ItemNotFoundException("Usuario no encontrado");
                });

        Task task = tasksRepository.findById(commentInput.getTaskId()).
                orElseThrow(()->{
                    throw  new ItemNotFoundException("Tarea no encontrada");
                });

       // TaskResponse taskResponse = tasksMapper.toResponse(task);
        UserResponse userResponse = userMapper.toResponse(user);

        Comment comment = Comment.builder()
                .user(user)
                .task(task)
                .content(commentInput.getContent())
                .build();

        commentRepository.save(comment);

        return commentMapper.fromEntityToResponse(comment,userResponse);
    }

    @Cacheable(value = "comment", key = "#id")
    @Transactional(readOnly = true)
    @Override
    public CommentResponse getCommentById(Long id) {


        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    throw new ItemNotFoundException("Comentario no encontrado");
                });

        User user = userRepository.findById(comment.getUser().getId())
                .orElseThrow(() -> {
                    throw new ItemNotFoundException("User no encontrado");
                });

        Task task = tasksRepository.findById(comment.getTask().getId())
                .orElseThrow(() -> {
                    throw new ItemNotFoundException("Tarea no encontrada");
                });

        return commentMapper.fromEntityToResponse(
                comment,
                userMapper.toResponse(user)
                //,tasksMapper.toResponse(task)
        );
    }

    @Cacheable(value = "commentsByTask", key = "#taskId")
    @Transactional(readOnly = true)
    @Override
    public List<CommentResponse> getCommentsByTaskId(Long taskId) {

        List<Comment> comments = commentRepository.findAllByTaskId(taskId);

        return comments.stream()
                .map(comment -> commentMapper.fromEntityToResponse(
                        comment,
                        userMapper.toResponse(comment.getUser())
                        //,tasksMapper.toResponse(comment.getTask())
                ))
                .collect(Collectors.toList());

    }

    @Override
    public List<CommentResponse> getAllComments() {

        List<Comment> comments = commentRepository.findAll();

        if(comments.isEmpty()){
            throw new ItemNotFoundException("No hay comentarios hasta el momento");

        }

        return comments.stream()
                .map(c -> commentMapper.fromEntityToResponse(
                            c,
                            userMapper.toResponse(c.getUser())
                            //,tasksMapper.toResponse(c.getTask())

                )).toList();
    }
}
