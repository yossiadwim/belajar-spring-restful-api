package com.example.belajar_spring_restful_api.controller;

import com.example.belajar_spring_restful_api.entity.Address;
import com.example.belajar_spring_restful_api.entity.Contact;
import com.example.belajar_spring_restful_api.entity.User;
import com.example.belajar_spring_restful_api.model.AddressResponse;
import com.example.belajar_spring_restful_api.model.CreateAddressRequest;
import com.example.belajar_spring_restful_api.model.UpdateAddressRequest;
import com.example.belajar_spring_restful_api.model.WebResponse;
import com.example.belajar_spring_restful_api.repository.AddressRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ContactRepository contactRepository;

    @BeforeEach
    void setUp() {
        addressRepository.deleteAll();
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
        user.setName("Test");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
        userRepository.save(user);

        Contact contact = new Contact();
        contact.setId("test");
        contact.setUser(user);
        contact.setFirstName("firstname");
        contact.setLastName("lastname");
        contact.setEmail("6Zq5b@example.com");
        contact.setPhone("08123456789");
        contactRepository.save(contact);

    }

    @Test
    void createAddressBadRequest() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setCountry("");

        mockMvc.perform(
                post("/api/contacts/test/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
            status().isBadRequest()
        ).andDo
                (
                        result -> {
                            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                            assertNotNull(response.getErrors());
                        }
                );

    }

    @Test
    void createAddressSuccess() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setStreet("street");
        request.setCity("city");
        request.setProvince("province");
        request.setCountry("country");
        request.setPostalCode("12345");

        mockMvc.perform(
                post("/api/contacts/test/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo
                (
                        result -> {
                            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                            assertNull(response.getErrors());
                            assertEquals("street", response.getData().getStreet());
                            assertEquals("city", response.getData().getCity());
                            assertEquals("province", response.getData().getProvince());
                            assertEquals("country", response.getData().getCountry());
                            assertEquals("12345", response.getData().getPostalCode());

                            assertTrue(addressRepository.existsById(response.getData().getId()));
                        }
                );

    }

    @Test
    void getAddressNotFound() throws Exception {
        mockMvc.perform(
                get("/api/contacts/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo
                (
                        result -> {
                            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                            assertNotNull(response.getErrors());
                        }
                );

    }


    @Test
    void getAddressSuccess() throws Exception {

        Contact contact = contactRepository.findById("test").orElseThrow();

        Address address = new Address();
        address.setId("test");
        address.setContact(contact);
        address.setStreet("street");
        address.setCity("city");
        address.setProvince("province");
        address.setCountry("country");
        address.setPostalCode("12345");
        addressRepository.save(address);

        mockMvc.perform(
                get("/api/contacts/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo
                (
                        result -> {
                            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                            assertNull(response.getErrors());
                            assertEquals("street", response.getData().getStreet());
                            assertEquals("city", response.getData().getCity());
                            assertEquals("province", response.getData().getProvince());
                            assertEquals("country", response.getData().getCountry());
                            assertEquals("12345", response.getData().getPostalCode());
                        }
                );

    }

    @Test
    void updateAddressBadRequest() throws Exception {
        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setCountry("");

        mockMvc.perform(
                put("/api/contacts/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo
                (
                        result -> {
                            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                            assertNotNull(response.getErrors());
                        }
                );

    }


    @Test
    void updateAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("test").orElseThrow();

        Address address = new Address();
        address.setId("test");
        address.setContact(contact);
        address.setStreet("street");
        address.setCity("city");
        address.setProvince("province");
        address.setCountry("country");
        address.setPostalCode("12345");
        addressRepository.save(address);

        CreateAddressRequest request = new CreateAddressRequest();
        request.setStreet("street 2");
        request.setCity("city 2");
        request.setProvince("province 2");
        request.setCountry("country 2");
        request.setPostalCode("123456");

        mockMvc.perform(
                put("/api/contacts/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo
                (
                        result -> {
                            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                            assertNull(response.getErrors());
                            assertEquals("street 2", response.getData().getStreet());
                            assertEquals("city 2", response.getData().getCity());
                            assertEquals("province 2", response.getData().getProvince());
                            assertEquals("country 2", response.getData().getCountry());
                            assertEquals("123456", response.getData().getPostalCode());

                            assertTrue(addressRepository.existsById(response.getData().getId()));
                        }
                );

    }

    @Test
    void deleteAddressNotFound() throws Exception {
        mockMvc.perform(
                delete("/api/contacts/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo
                (
                        result -> {
                            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                            assertNotNull(response.getErrors());
                        }
                );

    }


    @Test
    void deleteAddressSuccess() throws Exception {

        Contact contact = contactRepository.findById("test").orElseThrow();

        Address address = new Address();
        address.setId("test");
        address.setContact(contact);
        address.setStreet("street");
        address.setCity("city");
        address.setProvince("province");
        address.setCountry("country");
        address.setPostalCode("12345");
        addressRepository.save(address);

        mockMvc.perform(
                delete("/api/contacts/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo
                (
                        result -> {
                            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                            assertNull(response.getErrors());
                            assertEquals("OK", response.getData());

                            assertFalse(addressRepository.existsById("test"));
                        }
                );
    }

    @Test
    void listAddressNotFound() throws Exception {
        mockMvc.perform(
                get("/api/contacts/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo
                (
                        result -> {
                            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                            assertNotNull(response.getErrors());
                        }
                );

    }

    @Test
    void listAddressSuccess() throws Exception {

        Contact contact = contactRepository.findById("test").orElseThrow();

        for (int i = 0; i < 5; i++) {
            Address address = new Address();
            address.setId("test " + i);
            address.setContact(contact);
            address.setStreet("street");
            address.setCity("city");
            address.setProvince("province");
            address.setCountry("country");
            address.setPostalCode("12345");
            addressRepository.save(address);
        }

        mockMvc.perform(
                get("/api/contacts/test/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo
                (
                        result -> {
                            WebResponse<List<AddressResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                            assertNull(response.getErrors());
                            assertEquals(5, response.getData().size());
                        }
                );

    }

}