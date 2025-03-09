package com.example.belajar_spring_restful_api.controller;

import com.example.belajar_spring_restful_api.entity.User;
import com.example.belajar_spring_restful_api.model.LoginUserRequest;
import com.example.belajar_spring_restful_api.model.TokenResponse;
import com.example.belajar_spring_restful_api.model.WebResponse;
import com.example.belajar_spring_restful_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<TokenResponse> login(@RequestBody LoginUserRequest loginUserRequest) {
        TokenResponse tokenResponse = authService.login(loginUserRequest);
        return WebResponse.<TokenResponse>builder().data(tokenResponse).build();
    }

    @DeleteMapping(path = "/api/auth/logout",produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> logout(User user) {
        authService.logout(user);
        return WebResponse.<String>builder().data("OK").build();
    }
}
