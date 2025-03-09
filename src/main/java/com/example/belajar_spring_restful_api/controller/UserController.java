package com.example.belajar_spring_restful_api.controller;

import com.example.belajar_spring_restful_api.entity.User;
import com.example.belajar_spring_restful_api.model.RegisterUserRequest;
import com.example.belajar_spring_restful_api.model.UpdateUserRequest;
import com.example.belajar_spring_restful_api.model.UserResponse;
import com.example.belajar_spring_restful_api.model.WebResponse;
import com.example.belajar_spring_restful_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> register(@RequestBody RegisterUserRequest registerUserRequest) {
        userService.register(registerUserRequest);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(path = "/api/users/current",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<UserResponse> get(User user) {
        UserResponse userResponse = userService.get(user);
        return WebResponse.<UserResponse>builder().data(userResponse).build();
    }

    @PatchMapping(path = "/api/users/current",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<UserResponse> update(User user, @RequestBody UpdateUserRequest updateUserRequest) {

        UserResponse userResponse = userService.update(user, updateUserRequest);
        return WebResponse.<UserResponse>builder().data(userResponse).build();

    }
}
