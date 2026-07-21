package com.back.unit.services;
import com.back.entities.Notification;
import com.back.entities.User;
import com.back.entities.Workspace;
import com.back.entities.WorkspaceMember;
import com.back.entities.dto.CreateWorkspaceInput;
import com.back.entities.dto.NotificationResponse;
import com.back.entities.dto.WorkspaceResponse;
import com.back.entities.mappers.NotificationMapper;
import com.back.entities.mappers.WorkspaceMapper;
import com.back.entities.mappers.WorkspaceMemberMapper;
import com.back.enums.Role;
import com.back.exceptions.AlreadyExistException;
import com.back.exceptions.ItemNotFoundException;
import com.back.exceptions.UserNotFoundException;
import com.back.repositories.NotificationRepository;
import com.back.repositories.UserRepository;
import com.back.repositories.WorkspaceRepository;
import com.back.services.NotificationPublisherService;
import com.back.services.impl.WorkspaceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
public class WorkspaceServiceUnitTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkspaceMapper workspaceMapper;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationPublisherService notificationPublisherService;


    @InjectMocks
    private WorkspaceServiceImpl workspaceService;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private WorkspaceMemberMapper workspaceMemberMapper;





    @Test
    void shouldCreateSpaceSuccessfully(){

        UUID ownerId = UUID.randomUUID();

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        User member1 = new User();
        member1.setId(id1);
        User member2 = new User();
        member2.setId(id2);


        User owner = new User();
        owner.setName("Juan");
        owner.setEmail("gguan@gm.com");
        owner.setId(ownerId);

        CreateWorkspaceInput input = new CreateWorkspaceInput();
        input.setName("Plan B");
        input.setColor("#000555");
        input.setMembers_id(List.of(id1, id2));

        NotificationResponse notifResponse = new NotificationResponse();
        notifResponse.setMessage("Acepta cabroneta");

        when(userRepository.findById(ownerId)).thenReturn(
                Optional.of(owner)
        );

        when(workspaceRepository.existsByNameAndOwner_Id(input.getName(),ownerId))
                .thenReturn(false);

        when(userRepository.findAllById(input.getMembers_id()))
                .thenReturn(List.of(member1,member2));

        WorkspaceResponse response = new WorkspaceResponse();


        //eq equals igual a ..
        when(workspaceMapper.toResponse(any(Workspace.class), eq(3L), eq(0L)))
                .thenReturn(response);

        when(notificationMapper.toResponse(any(Notification.class)))
                .thenReturn(notifResponse);

        // Act
        WorkspaceResponse result = workspaceService.createWorkspace(input, ownerId);

        assertNotNull(result);

        //Verificar que se guarde las entidades 1 y 2 veces para su caso
        //Y que el publisher dos veces publique string 'userId o email' un Notification
        verify(workspaceRepository).save(any(Workspace.class));
        verify(notificationRepository, times(2)).save(any(Notification.class));
        verify(notificationPublisherService, times(2))
                .publish(anyString(), any(NotificationResponse.class));

    }


    @Test
    void shouldCreateSpaceUsersNotFound(){
        UUID ownerId = UUID.randomUUID();

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        User member1 = new User();
        member1.setId(id1);
        User member2 = new User();
        member2.setId(id2);


        User owner = new User();
        owner.setName("Juan");
        owner.setEmail("gguan@gm.com");
        owner.setId(ownerId);

        CreateWorkspaceInput input = new CreateWorkspaceInput();
        input.setName("Plan B");
        input.setColor("#000555");
        input.setMembers_id(List.of(id1, id2));

        NotificationResponse notifResponse = new NotificationResponse();
        notifResponse.setMessage("Acepta cabroneta");

        when(userRepository.findById(ownerId)).thenReturn(
                Optional.of(owner)
        );

        when(workspaceRepository.existsByNameAndOwner_Id(input.getName(),ownerId))
                .thenReturn(false);

        when(userRepository.findAllById(input.getMembers_id()))
                .thenReturn(List.of(member1));

        ItemNotFoundException exception = assertThrows(
                ItemNotFoundException.class,
                () -> workspaceService.createWorkspace(input,ownerId)
        );


        //Asegurar q devuelva el mismo error si sabe
        assertEquals("Uno o más usuarios no encontrados", exception.getMessage());

        verify(workspaceRepository, never()).save(any(Workspace.class));
        verify(notificationRepository, never()).save(any(Notification.class));
        verify(notificationPublisherService, never())
                .publish(anyString(), any(NotificationResponse.class));

    }



    @Test
    void shouldCreateSpaceOwnerNotFound(){
        UUID ownerId = UUID.randomUUID();
        CreateWorkspaceInput input = new CreateWorkspaceInput();
        input.setName("Plan B");
        input.setColor("#000555");


        when(userRepository.findById(ownerId))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> workspaceService.createWorkspace(input,ownerId)
        );


        assertEquals("Usuario no encontrado", exception.getMessage());

        verify(workspaceRepository, never()).save(any(Workspace.class));
        verify(notificationRepository, never()).save(any(Notification.class));
        verify(notificationPublisherService, never())
                .publish(anyString(), any(NotificationResponse.class));

    }
}
