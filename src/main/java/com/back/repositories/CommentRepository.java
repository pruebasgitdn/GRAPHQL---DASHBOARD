package com.back.repositories;

import com.back.entities.Comment;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {


    @Query("""
    SELECT c
    FROM Comment c
    JOIN FETCH c.user
    JOIN FETCH c.task
    WHERE c.task.id = :taskId
    """)
    List<Comment> findAllByTaskId(Long taskId);

    @Override
    @Nonnull
    @Query("""
    SELECT c
    FROM Comment c
    JOIN FETCH c.user
    JOIN FETCH c.task
    """)
    List<Comment> findAll();

}
