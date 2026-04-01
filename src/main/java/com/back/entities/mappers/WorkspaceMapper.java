package com.back.entities.mappers;

import com.back.entities.User;
import com.back.entities.Workspace;
import com.back.entities.dto.CreateWorkspaceInput;
import com.back.entities.dto.UserInput;
import com.back.entities.dto.WorkspaceResponse;
import com.back.repositories.WorkspaceMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorkspaceMapper {

    private final WorkspaceMemberRepository workspaceMemberRepository;

    public WorkspaceResponse toResponse(Workspace workspace) {
        long count = workspaceMemberRepository.countByWorkspaceId(workspace.getId());

        log.debug("DBG",count);

        return WorkspaceResponse.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .ownerId(workspace.getOwner().getId())
                .memberCount(count)
                .build();
    }

}

