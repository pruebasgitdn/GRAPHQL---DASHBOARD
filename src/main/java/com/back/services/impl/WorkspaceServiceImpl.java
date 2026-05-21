package com.back.services.impl;

import com.back.entities.Notification;
import com.back.entities.User;
import com.back.entities.Workspace;
import com.back.entities.WorkspaceMember;
import com.back.entities.dto.*;
import com.back.entities.mappers.NotificationMapper;
import com.back.entities.mappers.WorkspaceMapper;
import com.back.entities.mappers.WorkspaceMemberMapper;
import com.back.enums.NotificationType;
import com.back.enums.Role;
import com.back.exceptions.AlreadyExistException;
import com.back.exceptions.ItemNotFoundException;
import com.back.exceptions.UserNotFoundException;
import com.back.repositories.*;
import com.back.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationPublisherService notificationPublisher;

    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final ProjectRepository projectRepository;
    private final WorkspaceMemberService workspaceMemberService;
    private final UserRepository userRepository;

    private final WorkspaceMapper workspaceMapper;
    private final NotificationMapper notificationMapper;
    private final WorkspaceMemberMapper workspaceMemberMapper;

    private final HexColorGenerator colorGenerator;


    @Transactional
    @Override
    public WorkspaceResponse createWorkspace(CreateWorkspaceInput workspaceInput, UUID owner_id) {

        // Usuario owner
        User owner = userRepository.findById(owner_id).orElseThrow(()->{
            throw new UserNotFoundException();
        });

        if(workspaceRepository.existsByNameAndOwner_Id(workspaceInput.getName(),owner_id)){
            throw new AlreadyExistException("Ya tienes un workspace con ese nombre");
        }

        String color = workspaceInput.getColor();
        if (color == null || color.isEmpty()) {
            color = colorGenerator.generateRandomHexColor();
        }

        // Crear Workspace
        Workspace workspace = Workspace.builder()
                .name(workspaceInput.getName())
                .owner(owner)
                .color(color)
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



        //Buscar los ids de los usuarios y añadirlos
        List<User> allUsers = userRepository.findAllById(workspaceInput.getMembers_id());

        if(allUsers.size() != workspaceInput.getMembers_id().size()){
            throw new ItemNotFoundException("Uno o más usuarios no encontrados");
        }

        allUsers.forEach((p) -> {
            WorkspaceMember memberItem = WorkspaceMember.builder()
                    .user(p)
                    .role(Role.MEMBER)
                    .workspace(workspace)
                    .build();

            workspace.getMembers().add(memberItem);

        });
        workspaceRepository.save(workspace);


        Long memberCount = (long) workspace.getMembers().size();
        Long projectCount = 0L;

        workspace.getMembers().stream()
                .filter(m -> !m.getUser().getId().equals(owner_id)) // solo miembros, no owner
                .forEach(m -> {
                    Notification notification = Notification.builder()
                            .user(m.getUser())
                            .title("Nuevo en espacio de trabajo")
                            .message("Has sido añadido a un espacio de trabajo")
                            .type(NotificationType.COMMENT)
                            .build();

                    notificationRepository.save(notification);

                    NotificationResponse notifResp = notificationMapper.toResponse(notification);

                    WorkspaceMemberResponse wmResponse =
                            workspaceMemberMapper.toResponseCount(m, memberCount, projectCount);

                    notifResp.setWorkspaceMember(wmResponse);
                    notificationPublisher.publish(m.getUser().getId().toString(), notifResp);
                });


        return  workspaceMapper.toResponse(workspace,memberCount,projectCount);
    }

    @Transactional(readOnly = true)
    @Override
    public List<WorkspaceResponse> findAll() {

                return workspaceRepository.findAll()
                .stream()
                .map(p ->{
                    Long memberCount = workspaceMemberRepository.countByWorkspaceId(p.getId());
                    Long projectCount = projectRepository.countByWorkspaceId(p.getId());
                    return workspaceMapper.toResponse(p,memberCount,projectCount);
                })
                .toList();
    }



    @Override
    public WorkSpaceDetailResponse findById(UUID id) {

        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Espacio de trabajo no encontrado por el id: " + id));

        List<MemberRoleResponse> memberRoles = workspaceMemberService.getMembersFromWorkspace(workspace.getId());
        System.out.print(memberRoles);
        long memberCount = memberRoles.size();
        return  workspaceMapper.toDetailResponse(workspace,memberCount,memberRoles);
    }


    @Transactional
    @Override
    public WorkspaceResponse addMemberToWorkspace(List<UUID> users_id, UUID workspace_id,UUID owner_id) {

        //Verificar que el autenticado sea admin o owner
        boolean isAdminOrOwner = workspaceMemberService.isAdminOrOwner(owner_id,workspace_id);
        //System.out.println("Eres admin u owner? "+isAdminOrOwner);
        if (!isAdminOrOwner) {
            throw new RuntimeException("No tienes permisos");
        }


        if(users_id == null || users_id.isEmpty()){
            throw new RuntimeException("Lista vacía");
        }

        Set<UUID> uniqueUserIds = new HashSet<>(users_id);
        uniqueUserIds.remove(owner_id); //Excluir el id del dueño no queremos errores si sabe


        //Encontras usuarios
        List<User> usersToAdd = userRepository.findAllById(uniqueUserIds);
        if(usersToAdd.size() != uniqueUserIds.size() ){
            throw new ItemNotFoundException("Uno o mas IDs no existen");
        }



        //Encontrar grupo
        Workspace workspace = workspaceRepository.findById(workspace_id).orElseThrow(()->{
            throw  new ItemNotFoundException("Worspace no encontrado por el id : "+workspace_id);
        });



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


                    //Emitir notificacion
                    Notification notification = Notification.builder()
                            .user(user)
                            .title("Nuevo en espacio de trabajo")
                            .message("Has sido añadido a un espacio de trabajo")
                            .type(NotificationType.COMMENT)
                            .build();

                    NotificationResponse notificationResp = notificationMapper.toResponse(notification);


                    notificationPublisher.publish(user.getId().toString(),notificationResp);

                    notificationRepository.save(notification);

                    return wm;
                })
                .toList();

        //  guardar los miembros en el espacio de trabajo
        workspace.getMembers().addAll(newMembers);
        Long projectCount = projectRepository.countByWorkspaceId(workspace.getId());
        Long memberCount =  workspaceMemberRepository.countByWorkspaceId(workspace.getId());

        // Retornar
        return workspaceMapper.toResponse(workspace,memberCount,projectCount);
    }

    @Override
    public WorkspaceResponse addMemberToWorkspaceAfterLink(UUID userId, UUID workspace_id) {


        if(userId == null){
            throw  new RuntimeException("Ingresa el usuario");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->{
                    throw  new ItemNotFoundException("Usuario no encontrado");
                });

        Workspace workspace = workspaceRepository.findById(workspace_id)
                .orElseThrow(() ->{
                    throw  new ItemNotFoundException("Espacio de trabajo no encontrado");
                });


        WorkspaceMember wm = new WorkspaceMember();
        wm.setWorkspace(workspace);
        wm.setUser(user);
        wm.setRole(Role.MEMBER);

         workspace.getMembers().add(wm);

         Long projectCount =projectRepository.countByWorkspaceId(workspace.getId());
        Long memberCount =  workspaceMemberRepository.countByWorkspaceId(workspace.getId());

        return workspaceMapper.toResponse(workspace,memberCount,projectCount);
    }

    @Override
    @Transactional
    public WorkspaceResponse removeMembersFromWorkspace(List<UUID> users, UUID workspace_id, UUID owner_id) {

        //Verificar que el autenticado sea admin o owner
        boolean isAdminOrOwner = workspaceMemberService.isAdminOrOwner(owner_id,workspace_id);
        System.out.println("Eres admin u owner? "+isAdminOrOwner);
        if (!isAdminOrOwner) {
            throw new RuntimeException("No tienes permisos");
        }

        if(users == null || users.isEmpty()){
            throw new RuntimeException("Lista vacía");
        }

        Set<UUID> uniqueUserIds = new HashSet<>(users);

        uniqueUserIds.remove(owner_id); //Excluir el id del dueño no queremos errores si sabe

        //Verificar users
        List<User> usersToRemove = userRepository.findAllById(users);
        if(usersToRemove.size() != users.size() ){
            throw new ItemNotFoundException("Uno o mas IDs no existen");
        }

        //Encontrar workspace
        Workspace workspace = workspaceRepository.findById(workspace_id).orElseThrow(()->{
            throw  new ItemNotFoundException("Worspace no encontrado por el id : "+workspace_id);
        });

        //Pertenencia al grupo verific
        Set<UUID> workspaceUserIds = workspace.getMembers().stream()
                .map(wm -> wm.getUser().getId())
                .collect(Collectors.toSet());
        if(!workspaceUserIds.containsAll(uniqueUserIds)){
            throw new RuntimeException("Usuarios no pertenecen al workspace: "+workspace.getName());
        }

        //elimnar de la relacion
        workspace.getMembers().removeIf(wm ->
                uniqueUserIds.contains(wm.getUser().getId())
        );

        long memberCount = workspaceMemberRepository.countByWorkspaceId(workspace.getId());
        long projectCount = projectRepository.countByWorkspaceId(workspace.getId());


        return workspaceMapper.toResponse(workspace,memberCount,projectCount);
    }

    @Transactional
    @Override
    public Boolean removeWorkspace(UUID workspace_id,UUID owner_id) {

        //Verificar que el autenticado sea admin o owner
        boolean isAdminOrOwner = workspaceMemberService.isAdminOrOwner(owner_id,workspace_id);
        System.out.println("Eres admin u owner? "+isAdminOrOwner);
        if (!isAdminOrOwner) {
            throw new RuntimeException("No tienes permisos");
        }


        //Encontrar workspace
        Workspace workspace = workspaceRepository.findById(workspace_id).orElseThrow(()->{
            throw  new ItemNotFoundException("Worspace no encontrado por el id : "+workspace_id);
        });


        //eliminar de la relacion todos los usuarios asociados a dicho tan
        workspace.getMembers().clear();

        workspaceRepository.deleteById(workspace_id);

        return true;



    }
}
