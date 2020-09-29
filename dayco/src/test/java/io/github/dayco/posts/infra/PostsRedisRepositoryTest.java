package io.github.dayco.posts.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PostsRedisRepositoryTest {

    @Autowired
    private PostsRedisRepository postsRedisRepository;

    @Test
    public void increaseLikeCount_True() {
        postsRedisRepository.increaseLikeCount(1L, "test");

        Long likeCount = postsRedisRepository.getLikeCount(1L);
        assertEquals(likeCount, 1L);
    }

}
