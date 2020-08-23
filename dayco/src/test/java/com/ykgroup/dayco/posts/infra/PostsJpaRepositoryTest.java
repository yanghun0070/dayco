package com.ykgroup.dayco.posts.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ykgroup.dayco.posts.domain.Posts;

@SpringBootTest
public class PostsJpaRepositoryTest {

    @Autowired
    private PostsJpaRepository postJpaRepository;

    @AfterEach
    public void cleanUp() {
        postJpaRepository.deleteAll();
    }

    @Test
    public void savePosts_Equals_True() {
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postJpaRepository.save(Posts.builder()
                               .title(title)
                               .content(content)
                               .author("yanghun007@gmail.com")
                               .build());

        final List<Posts> postsList = postJpaRepository.findAll();

        Posts posts = postsList.get(0);
        assertEquals(posts.getTitle(), title);
        assertEquals(posts.getContent(), content);
    }


}
