package com.back.entities.mappers;

import com.back.entities.User;
import com.back.entities.Workspace;
import com.back.entities.dto.*;
import com.back.repositories.WorkspaceMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorkspaceMapper {

    private final WorkspaceMemberRepository workspaceMemberRepository;

    public WorkspaceResponse toResponseWithoutCount(Workspace workspace) {


        return WorkspaceResponse.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .ownerId(workspace.getOwner().getId())
                .build();
    }

    public WorkspaceResponse toResponse(Workspace workspace,Long memberCount ,Long projectCount) {


        return WorkspaceResponse.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .ownerId(workspace.getOwner().getId())
                .memberCount(memberCount)
                .projectCount(projectCount)
                .build();
    }

    public Workspace toEntityWithoutCount(WorkspaceResponse workspace) {


        return Workspace.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .build();
    }

    public WorkSpaceDetailResponse toDetailResponse(Workspace workspace, Long memberCount) {

        List<ProjectSimpleResponse> projects = workspace.getProjects().stream()
                .map(p -> ProjectSimpleResponse.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .description(p.getDescription())
                        .tasksCount((long) p.getTasks().size())
                        .build())
                .toList();

        return WorkSpaceDetailResponse.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .owner(workspace.getOwner())
                .memberCount(memberCount)
                .projects(projects)
                .build();
    }

}

