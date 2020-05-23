package com.ykgroup.dayco.uaa.user.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
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

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String authorizationHeader;

    @BeforeEach
    public void init() throws Exception {
        this.mockMvc.perform(
                post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON).content(
                        objectMapper.writeValueAsString(
                                new SessionUser("username", "a@n.m", "password"))
                ))
                    .andDo(print())
                    .andExpect(status().isOk());
        MvcResult result = mockMvc.perform(post("/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        new SessionUser("username",
                                                        "a@n.m",
                                                                  "password"))))
                                 .andDo(print())
                                 .andReturn();
        authorizationHeader = result.getResponse().getHeader("Authorization");
    }

    @Test
    public void testUser() throws Exception {
        this.mockMvc.perform(get("/user/username")
                                     .header("Authorization", authorizationHeader))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value("username"))
                    .andExpect(jsonPath("$.password").isNotEmpty())
                    .andExpect(jsonPath("$.userAuthorizations").isArray())
                    .andExpect(jsonPath("$.userAuthorizations[0].roleName").value("USER"));
    }

    @Test
    public void testUserThenSuccessful() throws Exception {
        this.mockMvc.perform(get("/user/wrongname")
                                     .header("Authorization", authorizationHeader))
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful());
    }
}
