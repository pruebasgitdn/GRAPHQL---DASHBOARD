package com.back.repositories;

import com.back.entities.User;
import com.back.entities.dto.UserTaskStatsDTO;
import com.back.entities.dto.UserTaskStatsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {


    //
    Optional<User> findByEmail(String email);

    List<User> findAllByEmailIn(Set<String> email);


    //Todas las asignaciones de todos los tan, General
    @Query(
     """
     SELECT  (
              u.id,
                     u.email,
                     u.name,
                     SUM(CASE WHEN t.priority = 'TODO' THEN 1 ELSE 0 END),
                     SUM(CASE WHEN t.priority = 'DONE' THEN 1 ELSE 0 END),
                     SUM(CASE WHEN t.priority = 'BLOCKED' THEN 1 ELSE 0 END),
                     SUM(CASE WHEN t.priority = 'IN_PROGRESS' THEN 1 ELSE 0 END)
                 )
                 FROM User u
                 JOIN u.taskAssignees ta
                 JOIN ta.task t
                 GROUP BY u.id, u.email, u.name

     """
    )
    List<UserTaskStatsDTO> getAllUserStatsTask();



}
