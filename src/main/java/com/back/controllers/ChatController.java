package com.back.controllers;


import com.back.entities.dto.PrivateMessageRequest;
import com.back.entities.dto.PrivateMessageResponse;
import com.back.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {



    // componente para enviar mensajes en tiempo real mediante
    // websocket usando protocolos como stomp
    private final SimpMessagingTemplate messagingTemplate;


    //private final ChatMessageService chatMessageService;


    @MessageMapping("/chat.private")
    public void sendPrivMessage(
            PrivateMessageRequest request,
            Principal authentication
    ) {
        if (authentication == null ) {
            throw new IllegalStateException(
                    "Usuario no autenticado"
            );
        }

        String username = authentication.getName();

var response = new PrivateMessageResponse(
        username,
        request.content()
);

System.out.println("Respuesta sendPRivMessage; "+response);
        messagingTemplate.convertAndSendToUser(
                request.receptorUsername(),
                "/queue/messages",
                response
        );
    }





    @MessageMapping("/chat00")
    private void getMessage(Message message){


        // El servicio valida la pertenencia a la conversación,
        // obtiene el remitente autenticado y persiste el mensaje.
       // ChatMessageEvent event =
         //       chatMessageService.send(input, authentication);

        // El evento solo se publica después de validar y guardar.
//        messagingTemplate.convertAndSend(
//                "/topic/conversations/" + event.conversationId(),
//                event);

        System.out.println("MENSAJE RECIBIDO"+ message);
    }


    @MessageMapping("/chat.send")
    public void sendMessage(
            String payload,
            Principal authentication
            ) {
    if (authentication == null ) {
            throw new IllegalStateException(
                    "Usuario no autenticado"
            );
        }

        String username = authentication.getName();

        String response = "User: "+ username+ " Message: "+payload;

        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/messages",
                response
        );
    }

}
