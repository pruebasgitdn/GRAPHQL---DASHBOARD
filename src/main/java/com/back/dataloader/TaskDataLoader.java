package com.back.dataloader;



import com.back.entities.SubTask;
import com.back.entities.Task;
import com.back.entities.mappers.TasksMapper;
import com.back.repositories.TasksRepository;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoaderEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.graphql.execution.DefaultBatchLoaderRegistry;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
@RequiredArgsConstructor
public class TaskDataLoader {

    private final TasksRepository tasksRepository;
    private final TasksMapper tasksMapper;
    public static final String TASK_LOADER = "tasksLoader";



    @Bean
    public RuntimeWiringConfigurer taskLoader(BatchLoaderRegistry registry) {

        registry.forTypePair(Long.class, List.class)
                .withName(TASK_LOADER) //Multiples <long> ids de los tan que los junta
                //en esa sola ejecucion para evitar el N+1
                .registerMappedBatchLoader((Set<Long> projectIds, BatchLoaderEnvironment env) -> {

                    List<Task> all = tasksRepository.findAllByProjectIdIn(projectIds);

                    Map<Long, List> grouped = new HashMap<>();

                    for (Task task : all) {
                        Long projectId = task.getProject().getId();  // ← POR PROJECT_ID

                        grouped.computeIfAbsent(projectId, k -> new ArrayList<>())
                                .add(tasksMapper.toResponseWithoutProject(task));
                    }

                    return Mono.just(grouped);
                });

        return wiringBuilder -> {};
    }




}
