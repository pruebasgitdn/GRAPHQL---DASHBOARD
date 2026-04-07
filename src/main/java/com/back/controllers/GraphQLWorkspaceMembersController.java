package com.back.controllers;
import com.back.entities.User;
import com.back.entities.Workspace;
import com.back.entities.WorkspaceMember;
import com.back.entities.dto.WorkspaceMemberResponse;
import com.back.entities.dto.WorkspaceMembersResponse;
import com.back.entities.dto.WorkspaceResponse;
import com.back.exceptions.ItemNotFoundException;
import com.back.repositories.UserRepository;
import com.back.repositories.WorkspaceRepository;
import com.back.services.WorkspaceMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Controller
@Slf4j
public class GraphQLWorkspaceMembersController {

    private final WorkspaceMemberService workspaceMemberService;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;


    @QueryMapping(name = "getMembersAndRoles")
    public List<WorkspaceMemberResponse> workspaceMembers(){

        return  workspaceMemberService.findAll();
    }

    @SchemaMapping(typeName = "WorkspaceMember", field = "user")
    public User user(WorkspaceMember wm) {
        return wm.getUser();
    }

    @SchemaMapping(typeName = "WorkspaceMember", field = "workspace")
    public Workspace workspace(WorkspaceMember wm) {
        return wm.getWorkspace();
    }



    @QueryMapping(name = "getWorkspaceMembers")
    public List<WorkspaceMemberResponse> getWorkspaceMembers(@Argument(name = "workspaceId") UUID workspaceId){
        return  workspaceMemberService.getWorkspaceUsers(workspaceId);
    }


    @QueryMapping(name = "getMemberWorkspaces")
    public List<WorkspaceMemberResponse> getMemberWorkspaces(@Argument(name = "memberId") UUID memberId){
        return  workspaceMemberService.getUserWorkspaces(memberId);
    }



//    @SchemaMapping(typeName = "WorkspaceMembersResponse", field = "users","workspace")
//    public WorkspaceMembersResponse resolveMembersAndWorkspace(WorkspaceMembersResponse workspaceMembersResponse) {
//        List<User> usersToAdd = userRepository.findAllById(users_id);
//        if(usersToAdd.size() != users_id.size() ){
//            throw new ItemNotFoundException("Uno o mas IDs no existen");
//        }
//
//        List<User> users = new ArrayList<User>(workspaceMembersResponse.getUsers());
//
//        List<UUID> uids = users.stream().map(User::getId).toList();
//
//        List<User> foundUsers = userRepository.findAllById(uids);
//
//        if () {
//            throw new ItemNotFoundException("Uno o mas usuarios no encontrados");
//
//        }
//
//
//        return
//    }



}
