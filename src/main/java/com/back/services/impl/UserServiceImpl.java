package com.back.services.impl;

import com.back.entities.User;
import com.back.entities.dto.UserInput;
import com.back.entities.mappers.UserMapper;
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
    public User registerUser(UserInput user) {

        //Encontrar por email
         Optional<User> userExist = userRepository.findByEmail(user.getEmail());

         if(userExist.isPresent()){
             throw new RuntimeException("El email: "+userExist.get().getEmail()+" ya esta en uso.");
         }


         //Mapper, codificar contraseña y retornar lo guardado
         User userToSave = userMapper.toEntity(user);
         userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
         return userRepository.save(userToSave);

    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
