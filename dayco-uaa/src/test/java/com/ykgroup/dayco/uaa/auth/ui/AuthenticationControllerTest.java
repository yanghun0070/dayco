package com.ykgroup.dayco.uaa.auth.ui;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ykgroup.dayco.uaa.auth.domain.AuthenticationRequest;
import com.ykgroup.dayco.uaa.manager.domain.UserAuthorization;
import com.ykgroup.dayco.uaa.user.domain.User;
import com.ykgroup.dayco.uaa.user.infra.UserJpaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserJpaRepository userJpaRepository;

    @Before
    public void init() {
        User user = new User("user",
                 new BCryptPasswordEncoder().encode("password"),
                 99,
                 1,
                 "n@n.m");
        user.addUserAuthorization(new UserAuthorization(user, "USER"));
        when(userJpaRepository.findByUserId("user"))
                .thenReturn(Optional.of(user));
    }

    @Test
    public void signInWithValidUserThenSuccessful() throws Exception {
        mockMvc.perform(post("/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        new AuthenticationRequest("user",
                                                                  "password"))))
               .andDo(print())
               .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void signInWithInvalidUserThenUnauthenticated() throws Exception {
        mockMvc.perform(post("/auth/signin")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(
                                                      new AuthenticationRequest("invalid",
                                                                                "invalidpassword"))))
               .andDo(print())
               .andExpect(status().isUnauthorized());
    }

    @Test
    public void signInWithWrongPasswordThenUnauthenticated() throws Exception {
        mockMvc.perform(post("/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        new AuthenticationRequest("user",
                                                                  "wrongpassword"))))
               .andDo(print())
               .andExpect(status().isUnauthorized());
    }
}
