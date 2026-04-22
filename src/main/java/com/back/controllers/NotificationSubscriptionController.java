package com.back.controllers;


import com.back.entities.Notification;
import com.back.entities.User;
import com.back.entities.dto.NotificationResponse;
import com.back.security.UserDetailsImpl;
import com.back.services.NotificationPublisherService;
import graphql.GraphQLContext;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class NotificationSubscriptionController {



    private final NotificationPublisherService publisherService;

    @SubscriptionMapping(name = "notifications")
    public Flux<NotificationResponse> notifications(
           GraphQLContext context
    ){


        UserDetailsImpl auth =  (UserDetailsImpl) context.get("user");

        System.out.println("user: " + auth.getId());


        return publisherService.subscribe(auth.getId().toString())
                .doOnSubscribe(sub ->
                        System.out.println("SUSCRITO: " + auth.getId().toString())
                )
                .doOnNext(n ->
                        System.out.println(" NOTIFICACIÓN: " + n)
                )
                .doOnCancel(() ->
                        System.out.println(" DESUSCRITO: " + auth.getId().toString())
                );
    }

    @QueryMapping
    public List<String> allConnections() {
        return publisherService.getActiveUsers();
    }
//    public List<String> allConnections(){
//
//        return  publisherService.getActiveUsers();
//
//
//    }





}
