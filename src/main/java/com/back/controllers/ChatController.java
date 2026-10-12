package com.back.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {



    // componente para enviar mensajes en tiempo real mediante
    // websocket usando protocolos como stomp
    private final SimpMessagingTemplate messagingTemplate;


    //private final ChatMessageService chatMessageService;

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

}
