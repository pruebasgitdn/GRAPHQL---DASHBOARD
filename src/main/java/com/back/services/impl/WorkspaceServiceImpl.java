package com.back.services.impl;

import com.back.entities.User;
import com.back.entities.Workspace;
import com.back.entities.WorkspaceMember;
import com.back.entities.dto.CreateProjectInput;
import com.back.entities.dto.CreateWorkspaceInput;
import com.back.entities.dto.WorkspaceResponse;
import com.back.entities.mappers.WorkspaceMapper;
import com.back.enums.Role;
import com.back.exceptions.ItemNotFoundException;
import com.back.exceptions.UserNotFoundException;
import com.back.repositories.UserRepository;
import com.back.repositories.WorkspaceMemberRepository;
import com.back.repositories.WorkspaceRepository;
import com.back.services.WorkspaceMemberService;
import com.back.services.WorkspaceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final WorkspaceMemberService workspaceMemberService;
    private final UserRepository userRepository;

    private final WorkspaceMapper workspaceMapper;

    @Transactional
    @Override
    public WorkspaceResponse createWorkspace(CreateWorkspaceInput workspaceInput, UUID owner_id) {

        // Usuario owner
        User owner = userRepository.findById(owner_id).orElseThrow(()->{
            throw new UserNotFoundException();
        });

        // Crear Workspace
        Workspace workspace = Workspace.builder()
                .name(workspaceInput.getName())
                .owner(owner)
                .build();

        // Crear wspacemember
        WorkspaceMember member = WorkspaceMember.builder()
                .user(owner)
                .role(Role.ADMIN)
                .workspace(workspace)
                .build();

        //Asociar al espacio de trabajo y posteriormente guardar
        //En su propia entidad el registro miembro
        //No hace falta dos repo.save => este member se guardo con cascade ALL
        //y su relacion con esta tabla
        workspace.getMembers().add(member);

        //Opcional crear proyecto
        //Si se crear asocialro al wspace


        workspaceRepository.save(workspace);
        return  WorkspaceResponse.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .ownerId(owner.getId())
                .build();
    }

    @Override
    public List<WorkspaceResponse> findAll() {

        return workspaceRepository.findAll()
                .stream()
                .map(workspaceMapper::toResponse)
                .toList();

    }

    @Override
    public WorkspaceResponse findById(UUID id) {

        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Espacio de trabajo no encontrado por el id: " + id));

        long count = workspaceMemberRepository.countByWorkspaceId(id);

        return WorkspaceResponse.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .ownerId(workspace.getOwner().getId())
                .memberCount(count)
                .build();
    }


    @Transactional
    @Override
    public WorkspaceResponse addMemberToWorkspace(List<UUID> users_id, UUID workspace_id,UUID owner_id) {

        //Verificar que el autenticado sea admin o owner
        boolean isAdminOrOwner = workspaceMemberService.isAdminOrOwner(owner_id,workspace_id);
        System.out.println("Eres admin u owner? "+isAdminOrOwner);
        if (!isAdminOrOwner) {
            throw new RuntimeException("No tienes permisos");
        }

        //Encontras usuarios
        List<User> usersToAdd = userRepository.findAllById(users_id);
        if(usersToAdd.size() != users_id.size() ){
            throw new ItemNotFoundException("Uno o mas IDs no existen");
        }

        //Encontrar grupo
        Workspace workspace = workspaceRepository.findById(workspace_id).orElseThrow(()->{
            throw  new ItemNotFoundException("Worspace no encontrado por el id : "+workspace_id);
        });

        if(users_id == null || users_id.isEmpty()){
            throw new RuntimeException("Lista vacía");
        }


        //IDS de usuarios del workspace existente
        Set<UUID> existingUserIds = workspace.getMembers().stream()
                .map(wm -> wm.getUser().getId())
                .collect(Collectors.toSet());

        // Se recorre los users existentes se guardan los que no sean iguales
        // Al los usuarios existentes del espacio de trabajo actual
        List<WorkspaceMember> newMembers = usersToAdd.stream()
                .filter(user -> !existingUserIds.contains(user.getId()))
                .map(user -> {
                    WorkspaceMember wm = new WorkspaceMember();
                    wm.setWorkspace(workspace);
                    wm.setUser(user);
                    wm.setRole(Role.MEMBER);
                    return wm;
                })
                .toList();

        //  guardar los miembros en el espacio de trabajo
        workspace.getMembers().addAll(newMembers);

        // Retornar
        return workspaceMapper.toResponse(workspace);
    }
}
