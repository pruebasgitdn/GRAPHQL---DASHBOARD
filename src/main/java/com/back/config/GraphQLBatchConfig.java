//package com.back.config;
//
//
//import com.back.entities.mappers.SubTaskMapper;
//import com.back.repositories.SubTaskRepository;
//import com.back.repositories.TasksRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.graphql.execution.BatchLoaderRegistry;
//import org.springframework.graphql.execution.RuntimeWiringConfigurer;
//
//@Configuration
//@RequiredArgsConstructor
//public class GraphQLBatchConfig {
//
//    private final SubTaskRepository subTaskRepository;
//    private final TasksRepository taskRepository;
//    private final SubTaskMapper subTaskMapper;
//
//    @Bean
//    public RuntimeWiringConfigurer runtimeWiringConfigurer(
//            BatchLoaderRegistry registry
//    ) {
//
//        registerSubTaskLoader(registry);
//        registerTaskLoader(registry);
//
//        return wiringBuilder -> {};
//    }
//
//
//}
