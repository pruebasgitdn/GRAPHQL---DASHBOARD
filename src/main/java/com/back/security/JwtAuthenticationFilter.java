package com.back.security;
import com.back.services.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            //Extraer de los headers
            String token = extractToken(request);

            if(token != null){

                //validar
                UserDetails userDetails = authenticationService.validateToken(token);

                //autenticacion de spring
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, //principal: user authenticado
                        null,
                        userDetails.getAuthorities()
                );

                // aplicar la autenticaion al request
                // user autenticatdo => setearlo al contexto de autenticacion
                SecurityContextHolder.getContext().setAuthentication(authentication);

                //info extra => si es instancia de tan (si pq implemta la interfaz)
                //se setea en el request info extra de userId
                if(userDetails instanceof  UserDetailsImpl){
                    UUID id = ((UserDetailsImpl) userDetails).getId();
                    request.setAttribute("user_id", id);
                    log.info("user_id en el atributte request: {}", id);

                }

            }


        }catch (Exception ex){
            log.warn("Excepcion filtro de Autorizacion: "+ex);

        }

        filterChain.doFilter(request,response);

    }



    //Extraccion del token de los headers
    private String extractToken(HttpServletRequest request) {

        //De los headers obtenemos el Autho...
        String header = request.getHeader("Authorization");


        //Si dicho header es null o no empieza con Bearer, null
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }

        //Si contiene algo entonces extraemos el valor de Bearer
        // Que seria el token ejm: Bearer token123 A token123
        return header.substring(7);
    }

}
