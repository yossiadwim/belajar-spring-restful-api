package com.example.belajar_spring_restful_api.service;


import com.example.belajar_spring_restful_api.entity.User;
import com.example.belajar_spring_restful_api.model.LoginUserRequest;
import com.example.belajar_spring_restful_api.model.TokenResponse;
import com.example.belajar_spring_restful_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ValidationService validationService;


    @Transactional
    public TokenResponse login(LoginUserRequest loginUserRequest) {
        validationService.validate(loginUserRequest);

        User user = userRepository.findById(loginUserRequest.getUsername())
                .orElseThrow(()
                        -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password is wrong"));

        if (BCrypt.checkpw(loginUserRequest.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30);
            userRepository.save(user);
            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password is wrong");
        }
    }

    @Transactional
    public void logout(User user) {
        user.setToken(null);
        user.setTokenExpiredAt(null);
        userRepository.save(user);
    }
}
