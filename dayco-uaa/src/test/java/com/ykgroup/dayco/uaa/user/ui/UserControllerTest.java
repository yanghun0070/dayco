package com.ykgroup.dayco.uaa.user.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ykgroup.dayco.uaa.user.domain.User;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Order(1)
    @Test
    public void testJoin() throws Exception {
        this.mockMvc.perform(
                post("/user/join")
                        .contentType(MediaType.APPLICATION_JSON).content(
                        objectMapper.writeValueAsString(
                                new User("username", "password"))
                ))
                    .andDo(print())
                    .andExpect(status().isOk());
    }

    @Order(2)
    @Test
    public void testUser() throws Exception {
        this.mockMvc.perform(get("/user/username"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value("username"))
                    .andExpect(jsonPath("$.password").isNotEmpty())
                    .andExpect(jsonPath("$.userAuthorizations").isArray())
                    .andExpect(jsonPath("$.userAuthorizations[0].roleName").value("USER"));
    }

    @Order(3)
    @Test
    public void testUserThenSuccessful() throws Exception {
        this.mockMvc.perform(get("/user/wrongname"))
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful());
    }
}
