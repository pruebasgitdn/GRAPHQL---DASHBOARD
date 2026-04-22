package com.back.entities.mappers;
import com.back.entities.User;
import com.back.entities.WorkspaceMember;
import com.back.entities.dto.WorkspaceMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class WorkspaceMemberMapper {

    private final UserMapper userMapper;
    private final WorkspaceMapper workspaceMapper;

   public WorkspaceMemberResponse toResponseCount (WorkspaceMember workspaceMember,Long memberCount, Long projectCount){


        return WorkspaceMemberResponse.builder()
                .id(workspaceMember.getId())
                .role(workspaceMember.getRole())
                .user(userMapper.toResponse(workspaceMember.getUser()))
                .workspace(workspaceMapper.toResponse((workspaceMember.getWorkspace()),memberCount,projectCount))
                .build();
    }

    public WorkspaceMemberResponse toResponse (WorkspaceMember workspaceMember){


        return WorkspaceMemberResponse.builder()
                .id(workspaceMember.getId())
                .role(workspaceMember.getRole())
                .user(userMapper.toResponse(workspaceMember.getUser()))
                .workspace(workspaceMapper.toResponseWithoutCount((workspaceMember.getWorkspace())))
                .build();
    }


}
