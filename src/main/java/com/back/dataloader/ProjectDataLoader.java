package com.back.dataloader;
import com.back.entities.Project;
import com.back.entities.mappers.ProjectMapper;
import com.back.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoaderEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;


@Component
@RequiredArgsConstructor
public class ProjectDataLoader {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    public static final String PROJECT_LOADER = "projectsLoader";


    @Bean
    public RuntimeWiringConfigurer projectLoader(BatchLoaderRegistry registry){


        registry.forTypePair(UUID.class, List.class)
                .withName(PROJECT_LOADER)
                .registerMappedBatchLoader((Set<UUID>  workspaceIds, BatchLoaderEnvironment env)->{

            List<Project> all = projectRepository.findAllByWorkspaceIdIn(workspaceIds);

            Map<UUID,List> grouped = new HashMap<>();

            for(Project project : all){
                UUID workspaceId = project.getWorkspace().getId();

                //computeifabsent asigna la clave valor solo si el la clave no existe
                //o su valor es nulo
                grouped.computeIfAbsent(workspaceId,k ->new ArrayList<>())
                        .add(projectMapper.toSimpleResponseWithoutCount(project));

            }
            return Mono.just(grouped);
        });

        return  wiringBuilder -> {};
    }


}
