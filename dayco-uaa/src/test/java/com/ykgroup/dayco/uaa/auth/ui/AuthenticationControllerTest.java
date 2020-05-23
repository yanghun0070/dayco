package com.ykgroup.dayco.uaa.auth.ui;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ykgroup.dayco.uaa.auth.dto.SessionUser;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Order(1)
    @Test
    public void signUpWithValidUserThenSuccessful() throws Exception {
        mockMvc.perform(post("/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        new SessionUser("user",
                                                        "a@n.m",
                                                        "password"))))
               .andDo(print())
               .andExpect(status().is2xxSuccessful());
    }

    @Order(2)
    @Test
    public void signInWithValidUserThenSuccessful() throws Exception {
        mockMvc.perform(post("/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        new SessionUser("user",
                                                        "a@n.m",
                                                                  "password"))))
               .andDo(print())
               .andExpect(status().is2xxSuccessful());
    }

    @Order(2)
    @Test
    public void signInWithInvalidUserThenUnauthenticated() throws Exception {
        mockMvc.perform(post("/auth/signin")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(
                                                      new SessionUser("invalid",
                                                                                "a@n.m",
                                                                                "invalidpassword"
                                                                                ))))
               .andDo(print())
               .andExpect(status().isUnauthorized());
    }

    @Order(2)
    @Test
    public void signInWithWrongPasswordThenUnauthenticated() throws Exception {
        mockMvc.perform(post("/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        new SessionUser("user",
                                                                  "a@n.m",
                                                                  "wrongpassword"
                                                                  ))))
               .andDo(print())
               .andExpect(status().isUnauthorized());
    }

    @Test
    public void refreshThenChangedToken() throws Exception {
        mockMvc.perform(post("/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        new SessionUser("testuser",
                                                        "a@n.m",
                                                        "testuser"
                                                                  ))))
               .andDo(print())
               .andExpect(status().is2xxSuccessful());
        MvcResult result = mockMvc.perform(post("/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        new SessionUser("testuser",
                                                        "a@n.m",
                                                        "testuser"
                                                                  ))))
                                  .andReturn();

        String authorizationHeader = result.getResponse().getHeader("Authorization");
        MvcResult refreshResult = mockMvc.perform(post("/auth/refresh")
                                .header("Authorization", authorizationHeader)
                                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        assertNotEquals(authorizationHeader, refreshResult.getResponse().getHeader("Authorization"));
    }
}