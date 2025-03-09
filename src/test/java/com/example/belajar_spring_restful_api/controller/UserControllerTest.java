package com.example.belajar_spring_restful_api.controller;

import com.example.belajar_spring_restful_api.entity.User;
import com.example.belajar_spring_restful_api.model.RegisterUserRequest;
import com.example.belajar_spring_restful_api.model.UpdateUserRequest;
import com.example.belajar_spring_restful_api.model.UserResponse;
import com.example.belajar_spring_restful_api.model.WebResponse;
import com.example.belajar_spring_restful_api.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;


@SpringBootTest
@AutoConfigureMockMvc

class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() throws Exception {

        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Test1");
        registerUserRequest.setPassword("password");
        registerUserRequest.setName("Testing Satu");


        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserRequest))

        ).andExpectAll(
                status().isOk()

        ).andDo(
                result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    assertEquals("OK", response.getData());
                }
        );
    }

    @Test
    void testRegisterFailed() throws Exception {

        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("");
        registerUserRequest.setPassword("");
        registerUserRequest.setName("");


        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserRequest))

        ).andExpectAll(
                status().isBadRequest()

        ).andDo(
                result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    assertNotNull(response.getErrors());
                }
        );
    }


    @Test
    void testRegisterDuplicate() throws Exception {

        User user = new User();
        user.setUsername("Test1");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setName("Testing Satu");
        userRepository.save(user);

        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("Test1");
        registerUserRequest.setPassword("password");
        registerUserRequest.setName("Testing Satu");


        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserRequest))

        ).andExpectAll(
                status().isBadRequest()

        ).andDo(
                result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    assertNotNull(response.getErrors());
                }
        );
    }

    @Test
    void getUserUnauthorized() throws Exception {

        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "notfound")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(
                result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse()
                            .getContentAsString(), new TypeReference<>() {
                    });

                    assertNotNull(response.getErrors());
                }
        );
    }

    @Test
    void getUserUnauthorizedTokenNotSend() throws Exception {

        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(
                result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse()
                            .getContentAsString(), new TypeReference<>() {
                    });

                    assertNotNull(response.getErrors());
                }
        );
    }

    @Test
    void getUserSuccess() throws Exception {

        User user = new User();
        user.setUsername("Test1");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setName("Testing Satu");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
        userRepository.save(user);

        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse()
                            .getContentAsString(), new TypeReference<>() {
                    });

                   assertNull(response.getErrors());
                   assertEquals("Test1", response.getData().getUsername());
                   assertEquals("Testing Satu", response.getData().getName());
                }
        );
    }

    @Test
    void getUserTokenExpired() throws Exception {

        User user = new User();
        user.setUsername("Test1");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setName("Testing Satu");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() - 1000000000);
        userRepository.save(user);

        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(
                result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse()
                            .getContentAsString(), new TypeReference<>() {
                    });

                    assertNotNull(response.getErrors());

                }
        );
    }


    @Test
    void updateUserUnauthorized() throws Exception {

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();

        mockMvc.perform(
                patch("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequest))

        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(
                result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse()
                            .getContentAsString(), new TypeReference<>() {
                    });

                    assertNotNull(response.getErrors());

                }
        );
    }


    @Test
    void updateUserSuccess() throws Exception {

        User user = new User();
        user.setUsername("Test1");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setName("Testing Satu");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
        userRepository.save(user);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setName("Testing Dua");
        updateUserRequest.setPassword("passwordbaru");


        mockMvc.perform(
                patch("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequest))
                        .header("X-API-TOKEN", "test")

        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse()
                            .getContentAsString(), new TypeReference<>() {
                    });

                    assertNull(response.getErrors());
                    assertEquals("Testing Dua", response.getData().getName());
                    assertEquals("Test1", response.getData().getUsername());


                    User userDb = userRepository.findById("Test1").orElse(null);
                    assert userDb != null;
                    assertTrue(BCrypt.checkpw("passwordbaru", userDb.getPassword()));

                }
        );
    }
}