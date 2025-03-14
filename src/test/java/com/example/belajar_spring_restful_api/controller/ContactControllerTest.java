package com.example.belajar_spring_restful_api.controller;

import com.example.belajar_spring_restful_api.entity.Contact;
import com.example.belajar_spring_restful_api.entity.User;
import com.example.belajar_spring_restful_api.model.ContactResponse;
import com.example.belajar_spring_restful_api.model.CreateContactRequest;
import com.example.belajar_spring_restful_api.model.UpdateContactRequest;
import com.example.belajar_spring_restful_api.model.WebResponse;
import com.example.belajar_spring_restful_api.repository.ContactRepository;
import com.example.belajar_spring_restful_api.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;


@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
        user.setName("Test");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
        userRepository.save(user);
    }

    @Test
    void createContactBadRequest() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("");
        request.setEmail("salah");

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(
                                objectMapper.writeValueAsString(request)).header("X-API-TOKEN", "test")
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
    void createContactSuccess() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("firstname");
        request.setLastName("lastname");
        request.setEmail("6Zq5b@example.com");
        request.setPhone("08123456789");

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(
                                objectMapper.writeValueAsString(request)).header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals("firstname", response.getData().getFirstName());
                    assertEquals("lastname", response.getData().getLastName());
                    assertEquals("6Zq5b@example.com", response.getData().getEmail());
                    assertEquals("08123456789", response.getData().getPhone());

                    assertTrue(contactRepository.existsById(response.getData().getId()));
                }
        );

    }

    @Test
    void getContactNotFound() throws Exception {

        mockMvc.perform(
                get("/api/contacts/124242")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                             .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(
                result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(response.getErrors());
                }
        );

    }

    @Test
    void getContactSuccess() throws Exception {
        User user = userRepository.findById("test").orElseThrow();

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setUser(user);
        contact.setFirstName("firstname");
        contact.setLastName("lastname");
        contact.setEmail("6Zq5b@example.com");
        contact.setPhone("08123456789");
        contactRepository.save(contact);

        mockMvc.perform(
                get("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals(contact.getId(), response.getData().getId());
                    assertEquals("firstname", response.getData().getFirstName());
                    assertEquals("lastname", response.getData().getLastName());
                    assertEquals("6Zq5b@example.com", response.getData().getEmail());
                    assertEquals("08123456789", response.getData().getPhone());
                }
        );

    }

    @Test
    void updateContactBadRequest() throws Exception {
        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("");
        request.setEmail("salah");

        mockMvc.perform(
                put("/api/contacts/1234")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(
                                objectMapper.writeValueAsString(request)).header("X-API-TOKEN", "test")
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
    void updateContactSuccess() throws Exception {
        User user = userRepository.findById("test").orElseThrow();

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setUser(user);
        contact.setFirstName("firstname");
        contact.setLastName("lastname");
        contact.setEmail("6Zq5b@example.com");
        contact.setPhone("08123456789");
        contactRepository.save(contact);

        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("firstname 1");
        request.setLastName("lastname 1");
        request.setEmail("flname@example.com");
        request.setPhone("0812345678910");

        mockMvc.perform(
                put("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON).content(
                                objectMapper.writeValueAsString(request)).header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals("firstname 1", response.getData().getFirstName());
                    assertEquals("lastname 1", response.getData().getLastName());
                    assertEquals("flname@example.com", response.getData().getEmail());
                    assertEquals("0812345678910", response.getData().getPhone());

                    assertTrue(contactRepository.existsById(response.getData().getId()));
                }
        );

    }

    @Test
    void deleteContactNotFound() throws Exception {

        mockMvc.perform(
                delete("/api/contacts/124242")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(
                result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(response.getErrors());
                }
        );

    }

    @Test
    void deleteContactSuccess() throws Exception {
        User user = userRepository.findById("test").orElseThrow();

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setUser(user);
        contact.setFirstName("firstname");
        contact.setLastName("lastname");
        contact.setEmail("6Zq5b@example.com");
        contact.setPhone("08123456789");
        contactRepository.save(contact);

        mockMvc.perform(
                delete("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals("OK", response.getData());
                }
        );

    }


    @Test
    void searchNotFound() throws Exception {

        mockMvc.perform(
                get("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals(0, response.getData().size());
                    assertEquals(0, response.getPaging().getTotalPage());
                    assertEquals(0, response.getPaging().getCurrentPage());
                    assertEquals(10, response.getPaging().getSize());
                }
        );

    }

    @Test
    void searchSuccess() throws Exception {

        User user = userRepository.findById("test").orElseThrow();

        for (int i = 0; i < 100; i++) {
            Contact contact = new Contact();
            contact.setId(UUID.randomUUID().toString());
            contact.setUser(user);
            contact.setFirstName("firstname " + i);
            contact.setLastName("lastname " + i);
            contact.setEmail("6Zq5b@example.com");
            contact.setPhone("08123456789");
            contactRepository.save(contact);
        }

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("name", "lastname")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse()
                            .getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals(10, response.getData().size());
                    assertEquals(0, response.getPaging().getCurrentPage());
                    assertEquals(10, response.getPaging().getTotalPage());
                    assertEquals(10, response.getPaging().getSize());
                }
        );


        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("email", "example.com")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse()
                            .getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals(10, response.getData().size());
                    assertEquals(0, response.getPaging().getCurrentPage());
                    assertEquals(10, response.getPaging().getTotalPage());
                    assertEquals(10, response.getPaging().getSize());
                }
        );


        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("phone", "08123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse()
                            .getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals(10, response.getData().size());
                    assertEquals(0, response.getPaging().getCurrentPage());
                    assertEquals(10, response.getPaging().getTotalPage());
                    assertEquals(10, response.getPaging().getSize());
                }
        );

    }





}
