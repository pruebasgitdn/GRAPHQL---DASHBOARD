package com.back.services.impl;

import com.back.entities.User;
import com.back.entities.dto.UserInput;
import com.back.entities.dto.UserResponse;
import com.back.entities.mappers.UserMapper;
import com.back.exceptions.ItemNotFoundException;
import com.back.repositories.UserRepository;
import com.back.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;




    @Override
    public UserResponse registerUser(UserInput user) {

        //Encontrar por email
         Optional<User> userExist = userRepository.findByEmail(user.getEmail());

         if(userExist.isPresent()){
             throw new RuntimeException("El email: "+userExist.get().getEmail()+" ya esta en uso.");
         }


         //Mapper, codificar contraseña y retornar lo guardado
         User userToSave = userMapper.toEntity(user);
         userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));

         userRepository.save(userToSave);

         return UserResponse.builder()
                 .id(userToSave.getId())
                 .name(userToSave.getName())
                 .email(userToSave.getEmail())
                 .profilePic(userToSave.getProfilePic())
                 .build();
    }

    @Override
    public UserResponse findById(UUID id) {

        Optional<User> uExist = userRepository.findById(id);

        if(uExist.isPresent()){
            return UserResponse.builder()
                    .id(uExist.get().getId())
                    .name(uExist.get().getName())
                    .email(uExist.get().getEmail())
                    .profilePic(uExist.get().getProfilePic())
                    .build();
        }
        throw  new ItemNotFoundException("User no encontrado");

         }

    @Override
    public List<UserResponse> findAll() {

        List<User> allU = userRepository.findAll();

        return userMapper.mapUsers(allU);
    }
}
