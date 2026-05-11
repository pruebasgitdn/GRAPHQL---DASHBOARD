package com.back.dataloader;


import com.back.entities.SubTask;
import com.back.entities.dto.SubTaskResponse;
import com.back.entities.mappers.SubTaskMapper;
import com.back.repositories.SubTaskRepository;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.BatchLoaderWithContext;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.graphql.execution.DefaultBatchLoaderRegistry;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubTaskDataLoader {


    private final SubTaskRepository subTaskRepository;
    private final SubTaskMapper subTaskMapper;
    public static final String SUBTASK_LOADER = "subTaskLoader";


//    @Bean
//    public BatchLoaderRegistry subTaskBatchLoader() {
//        DefaultBatchLoaderRegistry registry = new DefaultBatchLoaderRegistry();
//
//        //registrar => tipo long y list
//        registry.forTypePair(Long.class, List.class)
//                .withName(SUBTASK_LOADER) //nombre del metodo que invoca el dataloader
//                .registerMappedBatchLoader((taskIds, env) -> {
//
//                    List<SubTask> all = subTaskRepository.findAllByTaskIdIn(taskIds);
//
////                    Map<Long, List> grouped = all.stream()
////                            .collect(Collectors.groupingBy(
////                                    st -> st.getTask().getId(),
////                                    Collectors.mapping(subTaskMapper::toResponse,
////                                            Collectors.toCollection(ArrayList::new))
////                            ));
//
//                    Map<Long, List> grouped = new HashMap<>();
//                    for (SubTask subTask : all) {
//
//                        Long taskId = subTask.getTask().getId();
//                        // actua solo cuando la clave no esta presente en el mapa o cuando su valor asociado es null
//                        grouped.computeIfAbsent(taskId, k -> new ArrayList<>())
//                                .add(subTaskMapper.toResponse(subTask)); //Entonces lo añade
//                    }
//
//                    return Mono.just(grouped);
//                });
//
//        return registry;
//    }


//@Bean
//public RuntimeWiringConfigurer runtimeWiringConfigurer(
//        BatchLoaderRegistry registry
//) {
//
//    registry.forTypePair(Long.class, List.class)
//            .withName(SUBTASK_LOADER)
//            .registerMappedBatchLoader((Set<Long> taskIds, BatchLoaderEnvironment env) -> {
//
//                List<SubTask> all =
//                        subTaskRepository.findAllByTaskIdIn(taskIds);
//
//                Map<Long, List> grouped = new HashMap<>();
//                    for (SubTask subTask : all) {
//
//                        Long taskId = subTask.getTask().getId();
//                        // actua solo cuando la clave no esta presente en el mapa o cuando su valor asociado es null
//                        grouped.computeIfAbsent(taskId, k -> new ArrayList<>())
//                                .add(subTaskMapper.toResponse(subTask)); //Entonces lo añade
//                    }
//
//                return Mono.just(grouped);
//            });
//
//    return wiringBuilder -> {};
//
//}

@Bean
public RuntimeWiringConfigurer subTaskLoader(BatchLoaderRegistry registry) {

    registry.forTypePair(Long.class, List.class)
            .withName(SUBTASK_LOADER)
            .registerMappedBatchLoader((Set<Long> taskIds, BatchLoaderEnvironment env) -> {

                List<SubTask> all = subTaskRepository.findAllByTaskIdIn(taskIds);

                Map<Long, List> grouped = new HashMap<>();

                for (SubTask subTask : all) {
                    Long taskId = subTask.getTask().getId();

                    grouped.computeIfAbsent(taskId, k -> new ArrayList<>())
                            .add(subTaskMapper.toResponse(subTask));
                }

                return Mono.just(grouped);
            });

    return wiringBuilder -> {};
}



}