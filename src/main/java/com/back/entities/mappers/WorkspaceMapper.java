package com.back.entities.mappers;

import com.back.entities.User;
import com.back.entities.Workspace;
import com.back.entities.dto.CreateWorkspaceInput;
import com.back.entities.dto.UserInput;
import com.back.entities.dto.WorkspaceResponse;
import com.back.repositories.WorkspaceMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkspaceMapper {

    private final WorkspaceMemberRepository workspaceMemberRepository;

    public WorkspaceResponse toResponse(Workspace workspace) {
        long count = workspaceMemberRepository.countByWorkspaceId(workspace.getId());


        return WorkspaceResponse.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .ownerId(workspace.getOwner().getId())
                .memberCount(count)
                .build();
    }

}

