package com.back.config;

import com.back.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


//Verificar la autenticidad del usaurio para permitirle
//conectar, enviar y recibir mensajes atraves del protocolo STOMP

//El cliente manda envia info y interceptor recoge el header Authorization
// Valida el token y si es valido extrae el usuario y permite la conexion


@Component
@RequiredArgsConstructor
public class StompAuthenticationInterceptor  implements ChannelInterceptor {

    private final AuthenticationService authenticationService;



    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(
                        message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authHeader =
                    accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null ||
                    !authHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException(
                        "Falta el token de autenticación");
            }

            String token = authHeader.substring(7);

            if (!authenticationService.isAccessToken(token)) {
                throw new IllegalArgumentException(
                        "Token inválido o expirado");
            }

            UserDetails user =
                    authenticationService.validateToken(token);

            var authentication =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities());

            accessor.setUser(authentication);
        }

        return message;
    }

}
