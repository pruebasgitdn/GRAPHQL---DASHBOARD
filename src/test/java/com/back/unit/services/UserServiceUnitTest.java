package com.back.unit.services;
import com.back.entities.User;
import com.back.entities.dto.UserInput;
import com.back.entities.dto.UserResponse;
import com.back.entities.mappers.UserMapper;
import com.back.exceptions.AlreadyExistException;
import com.back.repositories.UserRepository;
import com.back.services.UserService;
import com.back.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


//Inicializacion de mockito para los tests
@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;



    //mvn test -Dtest=UserServiceUnitTest#shouldRegisterUserSuccessfully
    @Test
    void shouldRegisterUserSuccessfully(){
        // Organizar datos
        UserInput input = new UserInput();
        input.setName("Juan");
        input.setEmail("juan@test.com");
        input.setPassword("123455");
        input.setRepeatPassword("123455");


        when(userRepository.findByEmail(input.getEmail()))
                // Optional.empty() => No hay usuarios con ese email entonces seguir ...
                .thenReturn(Optional.empty());

        User entity = new User();
        entity.setName("Juan");
        entity.setEmail("juan@test.com");
        entity.setPassword("123455");


        when(userMapper.toEntity(input))
                .thenReturn(entity);

        when(passwordEncoder.encode("123455"))
                .thenReturn("PASSWORD_ENCRIPTADO");

        // Aplicar el injectdel metodo si sabe
        UserResponse response = userServiceImpl.registerUser(input);

        // Asertar o asegurar que ...
        assertEquals("Juan", response.getName());
        assertEquals("juan@test.com", response.getEmail());

        verify(userRepository).save(entity);
        verify(passwordEncoder).encode("123455");
    }

    @Test
    void shouldRegisterUserEmailInUse(){
        // Organizar datos
        UserInput input = new UserInput();
        input.setEmail("juan@test.com");

        User entity = new User();
        entity.setEmail("juan@test.com");

        when(userRepository.findByEmail(input.getEmail()))
                .thenReturn(Optional.of(entity));

        // Aplicar el injectdel metodo si sabe
        AlreadyExistException exception = assertThrows(
                AlreadyExistException.class,
                () -> userServiceImpl.registerUser(input)
        );

        //Asegurar q devuelva el mismo error si sabe
        assertEquals("El email: "+entity.getEmail()+" ya esta en uso.", exception.getMessage());

        //Verificar en el userrepo nunca guardar la clase User
        //Pq ya existe si sabe
        verify(userRepository, never()).save(any(User.class));

    }

    @Test
    void shouldRegisterUserPasswordsNoMatch(){
        // Organizar datos
        UserInput input = new UserInput();
        input.setName("Juan");
        input.setEmail("juan@test.com");
        input.setPassword("123455");
        input.setRepeatPassword("12344");

        when(userRepository.findByEmail(input.getEmail()))
                // Optional.empty() => No hay usuarios con ese email entonces seguir ...
                .thenReturn(Optional.empty());


        // Aplicar el injectdel metodo si sabe
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userServiceImpl.registerUser(input)
        );

        assertEquals("Las contraseñas no coinciden", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));


    }



}
