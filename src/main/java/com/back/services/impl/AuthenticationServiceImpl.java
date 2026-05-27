package com.back.services.impl;
import com.back.entities.User;
import com.back.entities.dto.AuthResponse;
import com.back.entities.dto.UserResponse;
import com.back.entities.mappers.UserMapper;
import com.back.exceptions.InvalidCredentialsException;
import com.back.exceptions.UserNotFoundException;
import com.back.repositories.UserRepository;
import com.back.services.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;



@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final CookieService cookieService;

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final NotificationPublisherService notificationPublisher;
    private final UserMapper userMapper;
    private final UserService userService;

    //private final EmailService emailService;


    @Value("${jwt.secret}")
    private String secretKey;
    private final Long jwtExpiryMs = 86400000L;


    @Override
    public AuthResponse authenticate(String email, String password) {

        //autentica
        try {
            authenticationManager.authenticate((
                    new UsernamePasswordAuthenticationToken(email, password)
            ));
        } catch (BadCredentialsException ex) {
            //throw new RuntimeException("Credenciales Incorrectas");
            throw new InvalidCredentialsException();
        } catch (UsernameNotFoundException ex) {
            //throw new RuntimeException("Usuario no encontrado");
            throw new UserNotFoundException();
        }

        // generar el token y retornarlo
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String jwtToken = generateToken(userDetails);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado por el email: {}" + email));



        UserResponse userResponse = userService.findById(user.getId());
        User  userEntity = userMapper.FromResponseToEntity(userResponse);
        //Conexion subscripcion sink
        //notificationPublisher.subscribe(userEntity.getId().toString());

        //emailService.sendEmail();

        return AuthResponse.builder()
                .token(jwtToken)
                .refreshToken(generateRefreshToken(userDetails))
                .userId(user.getId())
                .email(user.getEmail())
                .expiresIn(86000)
                .build();
    }

    @Override
    public String generateToken(UserDetails userDetails) {

        return Jwts.builder()
                .claim("type", "access")
                .setSubject(userDetails.getUsername()) //dueño del token
                .setIssuedAt(new Date(System.currentTimeMillis())) //fecha creacion
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000L))// fecha expiracion
                .signWith(getSigninKey(), SignatureAlgorithm.HS256) //firmado con y el algoritmo
                .compact();//Compacta y devuelve como String JWT


    }

    @Override
    public UserDetails validateToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token) //validar firma
                .getBody();

        String username = claims.getSubject();

        return userDetailsService.loadUserByUsername(username);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .claim("type", "refresh")
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis())) //fecha creacion
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiryMs))// renovacion
                .signWith(getSigninKey(), SignatureAlgorithm.HS256) //firmado con y el algoritmo
                .compact();
    }


    private Key getSigninKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String extractName(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigninKey()) //Obtener llave con la q se firmo
                .build()//Construir
                .parseClaimsJws(token)// verificar y validar token
                .getBody(); //extraer datos del jwt

        //se retorna lo que se obtiene del subject que se setea en el
        // generateToken cuando se firma el token
        return claims.getSubject();
    }


    private Claims extractClaimsAllowExpired(String token) {
        
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigninKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (ExpiredJwtException ex){
            return ex.getClaims();
        }
        
      
    }

    @Override
    public boolean isAccessToken(String token) {
        return "access".equals(extractClaimsAllowExpired(token).get("type"));
    }

    @Override
    public boolean isRefreshToken(String token) {
        return "refresh".equals(extractClaimsAllowExpired(token).get("type"));
    }

    @Override
    public AuthResponse refreshToken(String token) {

        if(!isRefreshToken(token)){
            throw  new RuntimeException("Token invalido");
        }

        //Extrar subject email q fue con lo q se firmo
        Claims claims = extractClaimsAllowExpired(token);
        String email = claims.getSubject();

        //Validar expiracion
        Date exp = claims.getExpiration();
        if(exp.before(new Date())){
            throw new RuntimeException("Refresh token expirado, inicia sesion de nuevo");
        }

        //Y encontrar los datos de ese perol por el gmail para setearlo
        //Si sabe
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        String newAccessToken = generateToken(userDetails);
        String newRefreshToken = generateRefreshToken(userDetails);
        return AuthResponse.builder()
                .token(newAccessToken)
                .refreshToken(newRefreshToken)
                .email(user.getEmail())
                .userId(user.getId())
                .expiresIn(86400)
                .build();

    }


}

