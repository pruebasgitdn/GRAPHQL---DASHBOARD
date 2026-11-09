package com.back.config;

import com.back.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.server.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class WebSocketConfig implements WebGraphQlInterceptor {

    private final AuthenticationService authenticationService;

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {

//        System.out.println("WSOCK HEADERS: " + request.getHeaders());

        //Extraer tkn
        String token = null;

        // Authorization del header
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

           String headerToken = authHeader.substring(7);

            if(authenticationService.isAccessToken(authHeader)){
                token = headerToken;
            }
        }

        // Cookies del header si no coge el de authorization
        if (token == null) {
            String cookie = request.getHeaders().getFirst("cookie");

            if (cookie != null) {
               String cookieToken = Arrays.stream(cookie.split(";"))
                        .map(String::trim)
                        .filter(c -> c.startsWith("sessionToken="))
                        .findFirst()
                        .map(c -> c.substring("sessionToken=".length()))
                        .orElse(null);

                if (cookieToken != null
                        && authenticationService.isAccessToken(cookieToken)) {
                    token = cookieToken;
                }

            }



        }

//        System.out.println("WSOCK TOKEN: " + token);

        // VALIDAR TOKEN
        if (token != null ) {

            UserDetails userDetails = authenticationService.validateToken(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            System.out.println("WSOCKET USER AUTHENTICATED: " + userDetails.getUsername());


            // INYECTAR CONTEXTO GRAPHQL + SECURITY

//
            //SecurituContext en los att del request del graph y ..
            // meter la autenticacion
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);

            request.configureExecutionInput((executionInput, builder) ->
                    builder.graphQLContext(Map.of("user", userDetails)).build()
            );

            return chain.next(request)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        }


        System.out.println("WSOCKET NO AUTH TOKEN FOUND");

        return chain.next(request);
    }
}