package com.example.belajar_spring_restful_api.service;

import com.example.belajar_spring_restful_api.entity.User;
import com.example.belajar_spring_restful_api.model.RegisterUserRequest;
import com.example.belajar_spring_restful_api.model.UpdateUserRequest;
import com.example.belajar_spring_restful_api.model.UserResponse;
import com.example.belajar_spring_restful_api.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.util.Objects;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator validator;

    @Autowired
    private ValidationService validationService;


    @Transactional
    public void register(RegisterUserRequest registerUserRequest) {

        validationService.validate(registerUserRequest);

        if (userRepository.existsById(registerUserRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        User user = new User();

        user.setUsername(registerUserRequest.getUsername());
        user.setPassword(BCrypt.hashpw(registerUserRequest.getPassword(), BCrypt.gensalt()));
        user.setName(registerUserRequest.getName());
        userRepository.save(user);

    }

    public UserResponse get(User user) {
        return UserResponse.builder().username(user.getUsername()).name(user.getName()).build();
    }


    @Transactional
    public UserResponse update(User user, UpdateUserRequest updateUserRequest) {
        validationService.validate(updateUserRequest);

        if(Objects.nonNull(updateUserRequest.getName())){
            user.setName(updateUserRequest.getName());
        }
        if (Objects.nonNull(updateUserRequest.getPassword())) {
            user.setPassword(BCrypt.hashpw(updateUserRequest.getPassword(), BCrypt.gensalt()));
        }

        userRepository.save(user);

        return UserResponse.builder().name(user.getName()).username(user.getUsername()).build();
    }




}
