package com.back.integration;
import com.back.entities.User;
import com.back.entities.dto.AuthResponse;
import com.back.repositories.UserRepository;
import com.back.services.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AuthenticationServiceIT {


    @Autowired
    private AuthenticationServiceImpl authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    //Al ejecutarse en cada test borra todos y crea uno para test obviamente
    //Lo q es necesario porq se necesita un usuario existente p
    //Para que el repository no marque que no existe el user
    @BeforeEach
    void setup() {

        userRepository.deleteAll();

        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword(passwordEncoder.encode("123456"));

        userRepository.save(user);
    }




    //mvn verify -Dit.test=YourIntegrationTestIT#Mytestname
    // mvn verify -Dit.test=AuthenticationServiceIT#shouldAuthenticateSuccessfully
    @Test
    void shouldAuthenticateSuccessfully(){
        AuthResponse response =
                authService.authenticate("test@test.com","123456");

        assertNotNull(response);

        assertNotNull(response.getToken());
        assertNotNull(response.getRefreshToken());

        assertEquals("test@test.com",response.getEmail());


        assertNotNull(response.getUserId());
        assertEquals(86000,response.getExpiresIn());



    }







}
