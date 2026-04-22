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

        System.out.println("WSOCK HEADERS: " + request.getHeaders());

        // =========================
        // 1. EXTRAER TOKEN
        // =========================
        String token = null;

        // Authorization header (PRIORIDAD)
        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        // Cookie fallback
        if (token == null) {
            String cookie = request.getHeaders().getFirst("cookie");

            if (cookie != null) {
                token = Arrays.stream(cookie.split(";"))
                        .map(String::trim)
                        .filter(c -> c.startsWith("sessionToken="))
                        .findFirst()
                        .map(c -> c.substring("sessionToken=".length()))
                        .orElse(null);
            }
        }

        System.out.println("WSOCK TOKEN: " + token);

        // 2. VALIDAR TOKEN
        if (token != null && authenticationService.isAccessToken(token)) {

            UserDetails userDetails = authenticationService.validateToken(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            System.out.println("WSOCKET USER AUTHENTICATED: " + userDetails.getUsername());


            // 3. INYECTAR CONTEXTO GRAPHQL + SECURITY

//            return chain.next(request)
//                    .contextWrite(
//                            ReactiveSecurityContextHolder.withAuthentication(authentication)
//                    );


            //SecurituContext en los att del request del graph
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