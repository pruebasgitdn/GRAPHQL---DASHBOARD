package com.back.services.impl;
import com.back.entities.User;
import com.back.entities.dto.AuthResponse;
import com.back.exceptions.GraphQLExceptionHandler;
import com.back.exceptions.InvalidCredentialsException;
import com.back.exceptions.UserNotFoundException;
import com.back.repositories.UserRepository;
import com.back.services.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;
    private final Long jwtExpiryMs = 86400000L;


    @Override
    public AuthResponse authenticate(String email, String password) {

        //autentica
     try{
         authenticationManager.authenticate((
                 new UsernamePasswordAuthenticationToken(email,password)
         ));
     } catch (BadCredentialsException ex) {
        //throw new RuntimeException("Credenciales Incorrectas");
        throw  new InvalidCredentialsException();
     }catch (UsernameNotFoundException ex) {
         //throw new RuntimeException("Usuario no encontrado");
            throw new UserNotFoundException();
     }

        // generar el token y retornarlo
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String jwtToken = generateToken(userDetails);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado por el email: {}"+email));


        return AuthResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .email(user.getEmail())
                .expiresIn(86000)
                .build();
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String,Object> claim = new HashMap<>();

        return Jwts.builder()
                .setClaims(claim) // claim
                .setSubject(userDetails.getUsername()) //dueño del token
                .setIssuedAt(new Date(System.currentTimeMillis())) //fecha creacion
                .setExpiration(new Date(System.currentTimeMillis()+jwtExpiryMs))// fecha expiracion
                .signWith(getSigninKey(), SignatureAlgorithm.HS256) //firmado con y el algoritmo
                .compact();//Compacta y devuelve como String JWT


    }

    @Override
    public UserDetails validateToken(String token) {
        String username = extractName(token);
        return  userDetailsService.loadUserByUsername(username);
    }


    private Key getSigninKey(){
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String extractName(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigninKey()) //Obtener llave con la q se firmo
                .build()//Construir
                .parseClaimsJws(token)// verificar y validar token
                .getBody(); //extraer datos del jwt

        //se retorna lo que se obtiene del subject que se setea en el
        // generateToken cuando se firma el token
        return claims.getSubject();
    }



}

