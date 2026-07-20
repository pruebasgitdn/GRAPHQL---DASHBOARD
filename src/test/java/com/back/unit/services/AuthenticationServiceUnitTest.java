package com.back.unit.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import com.back.entities.User;
import com.back.entities.dto.AuthResponse;
import com.back.exceptions.InvalidCredentialsException;
import com.back.exceptions.UserNotFoundException;
import com.back.repositories.UserRepository;
import com.back.services.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceUnitTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthenticationServiceImpl authService;



    //A cada test que corra se resetea
    //Y se hace el spy porq se nececsito algunos metodos
    @BeforeEach
    void setUp() {
        authService = Mockito.spy(authService);
    }

    @Test
    void shouldAuthenticateSuccessfully(){

        String email = "test@test.com";
        String password = "123456";

        //Como es una clase se hace un mock(Clase)
        UserDetails userDetails = mock(UserDetails.class);

        User user = new User();
        UUID userId = UUID.randomUUID();

        user.setId(userId);
        user.setEmail(email);

        //doreturn => retornar esto cuando se ejecute esto
        //porque si se hace con when then return mockito lo ejecuta
        //y esto se hace porque solo se quiere probar el authenticathe
        doReturn("jwt-token").when(authService).generateToken(userDetails);
        doReturn("refresh-token").when(authService).generateRefreshToken(userDetails);


        when(userDetailsService.loadUserByUsername(email))
                .thenReturn(userDetails);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        // Act, el injectmock y el spy trabajan especificamente aca
        // el injectmock junto con el spy porque esto si lo necesito
        // a lo otro le hago doreturn para evitar su ejecucion si sabe
        AuthResponse response = authService.authenticate(email, password);

        // Assert
        verify(authenticationManager).authenticate(
                any(UsernamePasswordAuthenticationToken.class));

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("refresh-token", response.getRefreshToken());
        assertEquals(email, response.getEmail());
        assertEquals(userId, response.getUserId());
        assertEquals(86000, response.getExpiresIn());


    }

    @Test
    void shouldThrowInvalidCredentialsException() {
        // Arrange
        String email = "test@test.com";
        String password = "123456";


        //dothrow para cuando un metodo necesito q lance una execption
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // verifica que este moetodo lanze esta expecion, ese error
        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.authenticate(email, password)
        );

        verify(userDetailsService, never()).loadUserByUsername(any());
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenAuthenticationFails() {
        // Arrange
        String email = "test@test.com";
        String password = "123456";

        doThrow(new UsernameNotFoundException("User not found"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act & Assert
        assertThrows(
                UserNotFoundException.class,
                () -> authService.authenticate(email, password)
        );


        //Verificar que no se llego hasta alla o no ejecuto eso
        //Porque no paso las excepciones
        verify(userDetailsService, never()).loadUserByUsername(any());
    }

}
