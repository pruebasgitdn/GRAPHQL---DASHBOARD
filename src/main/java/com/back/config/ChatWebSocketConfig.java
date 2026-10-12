package com.back.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class ChatWebSocketConfig implements WebSocketMessageBrokerConfigurer {


    private final StompAuthenticationInterceptor authInterceptor;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){

        //Esta es la url y el origen permitido del cliente
        //y el cual el cliente se conecta (/ws) para el handshake
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3000");
    }


@Override
    public void configureMessageBroker(MessageBrokerRegistry registry){


    //prefijos del cliente para que pueda enviar info
    registry.enableSimpleBroker("/topic","/queue");
    //topic : eventos compartidos,
    //queue : destinos de mensajes individuales


    registry.setApplicationDestinationPrefixes("/app"); // prefijo para que el cliente envie la info
    //conecta de una al controlador

    registry.setUserDestinationPrefix("/user"); //desitnos privados asociados al user authenticado

}



    @Override
    public void configureClientInboundChannel(
            ChannelRegistration registration) {
        registration.interceptors(authInterceptor);
    }

}
