package com.back.services.impl;

import com.back.entities.User;
import com.back.entities.Workspace;
import com.back.entities.WorkspaceInvitation;
import com.back.entities.dto.WorkspaceResponse;
import com.back.entities.mappers.WorkspaceMapper;
import com.back.enums.InvitationStatus;
import com.back.exceptions.AlreadyExistException;
import com.back.exceptions.ItemNotFoundException;
import com.back.repositories.UserRepository;
import com.back.repositories.WorkspaceInvitationRepository;
import com.back.repositories.WorkspaceMemberRepository;
import com.back.repositories.WorkspaceRepository;
import com.back.services.EmailService;
import com.back.services.WorkspaceService;
import com.back.services.WspaceInvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WspaceInvitationServiceImpl implements WspaceInvitationService {

    private final WorkspaceInvitationRepository workspaceInvitationRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final WorkspaceService workspaceService;
    private final WorkspaceMapper workspaceMapper;


    @Override
    public String inviteUserToWspace(UUID workspaceId, String email) {

        //Encontrar w y user
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->{
                    throw  new ItemNotFoundException("Este usuario no existe: "+email);
                });

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(()->{
                    throw  new ItemNotFoundException("Este espacio de trabajo no existe: "+workspaceId);
                });

        if(workspaceMemberRepository.existsByWorkspace_IdAndUser_Id(workspaceId,user.getId())){
            throw  new RuntimeException("Este usuario: "+user.getEmail()+", ya es miembro");
        }

        String token = UUID.randomUUID().toString();

        WorkspaceInvitation invitation = WorkspaceInvitation.builder()
                .token(token)
                .workspace(workspace)
                .invitedUser(user)
                .status(InvitationStatus.PENDING)
                .expiresAt(LocalDateTime.now().plusHours(2))
                .build();


        workspaceInvitationRepository.save(invitation);

        emailService.sendWorkspaceInvitation(email,token,workspace.getName());

        return "Invitación enviada a " + email+", Se espera su respuesta  ";
    }

    @Override
    public String sendMultipleInvitesToWspace(UUID workspaceId, List<String> emails) {


        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(()->{
                    throw  new ItemNotFoundException("Este espacio de trabajo no existe: "+workspaceId);
                });

        Set<String> uniqueUserEmails = new HashSet<>(emails);

        //Encontras usuarios
        List<User> usersToAdd = userRepository.findAllByEmailIn(uniqueUserEmails);
        if(usersToAdd.size() != uniqueUserEmails.size() ){
            throw new ItemNotFoundException("Uno o mas correos no existen");
        }


        usersToAdd.forEach(user -> {

            String token = UUID.randomUUID().toString();

            WorkspaceInvitation invitation = WorkspaceInvitation.builder()
                    .token(token)
                    .workspace(workspace)
                    .invitedUser(user)
                    .status(InvitationStatus.PENDING)
                    .expiresAt(LocalDateTime.now().plusHours(2))
                    .build();

            workspaceInvitationRepository.save(invitation);

            emailService.sendWorkspaceInvitation(
                    user.getEmail(),
                    token,
                    workspace.getName()
            );
        });

        return "Invitaciones enviadas con éxito";
    }

    @Override
    public String confirmInvitation(String token) {
        WorkspaceInvitation workspaceInvitation = workspaceInvitationRepository.findByToken(token)
                .orElseThrow(() -> new ItemNotFoundException(
                        "Invitacion no encontrada"));


        if(workspaceInvitation.getStatus() != InvitationStatus.PENDING){ //aceptada o expírada
            throw new AlreadyExistException("Esta invitacion ya fue usada");
        }

        if(workspaceInvitation.getExpiresAt().isBefore(LocalDateTime.now())){
        workspaceInvitation.setStatus(InvitationStatus.EXPIRED);
        workspaceInvitationRepository.save(workspaceInvitation);
        throw new RuntimeException("Esta invitacion ya expiró");
        }

        //añadir y guardar al wpsaceMembers
        WorkspaceResponse workspace = workspaceService.addMemberToWorkspaceAfterLink(
                workspaceInvitation.getInvitedUser().getId()
                ,workspaceInvitation.getWorkspace().getId());

//        Workspace workspacetoSave = workspaceMapper.toEntityWithoutCount(workspace);
//        workspaceRepository.save(workspacetoSave);

        //guardar aceptada
        workspaceInvitation.setStatus(InvitationStatus.ACCEPTED);
        workspaceInvitationRepository.save(workspaceInvitation);

        return "Te uniste a :"+workspaceInvitation.getWorkspace().getName()+"";
    }
}
