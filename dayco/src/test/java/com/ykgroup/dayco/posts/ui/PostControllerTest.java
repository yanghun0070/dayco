package com.ykgroup.dayco.posts.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ykgroup.dayco.posts.domain.Posts;
import com.ykgroup.dayco.posts.infra.PostsJpaRepository;
import com.ykgroup.dayco.posts.ui.dto.PostsSaveRequestDto;
import com.ykgroup.dayco.posts.ui.dto.PostsUpdateRequestDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private PostsJpaRepository postsJpaRepository;

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        postsJpaRepository.deleteAll();
    }

    @Test
    public void createPosts() throws Exception {
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                                                            .title(title)
                                                            .content(content)
                                                            .author("author")
                                                            .build();

        //when
        mvc.perform(post("/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(requestDto)))
           .andExpect(status().isOk());

        List<Posts> all = postsJpaRepository.findAll();
        assertEquals(all.get(0).getTitle(), title);
        assertEquals(all.get(0).getContent(), content);
    }

    @Test
    public void updatePosts() throws Exception {
        //given
        Posts savedPosts = postsJpaRepository.save(Posts.builder()
                                                     .title("title")
                                                     .content("content")
                                                     .author("author")
                                                     .build());

        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                                                                .title(expectedTitle)
                                                                .content(expectedContent)
                                                                .build();

        //when
        mvc.perform(put("/posts/" + updateId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(requestDto)))
           .andExpect(status().isOk());

        //then
        List<Posts> all = postsJpaRepository.findAll();
        assertEquals(all.get(0).getTitle(), expectedTitle);
        assertEquals(all.get(0).getContent(), expectedContent);
    }
}
