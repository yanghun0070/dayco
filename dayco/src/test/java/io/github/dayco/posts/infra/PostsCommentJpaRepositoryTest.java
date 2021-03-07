package io.github.dayco.posts.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import io.github.dayco.posts.domain.Posts;
import io.github.dayco.posts.domain.PostsComment;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class PostsCommentJpaRepositoryTest {

    @Autowired
    private PostsJpaRepository postsJpaRepository;

    @Autowired
    private PostsCommentJpaRepository postsCommentJpaRepository;

    @BeforeEach
    public void init() throws Exception {
        Posts addPosts = postsJpaRepository.save(
                new Posts("title", "content", "author", Optional.empty()));
        postsCommentJpaRepository.save(new PostsComment(addPosts, "test", "author"));
    }

    @AfterEach
    public void cleanUp() {
        postsCommentJpaRepository.deleteAll();
    }

    @Order(1)
    @Test
    public void findByPostsIdOrderByIdDesc_Equals_True() {
        Posts posts = postsJpaRepository.findAll().get(0);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<PostsComment> postsCommentPage = postsCommentJpaRepository.findByPostsIdOrderByIdDesc(posts.getId(), pageRequest);
        assertEquals(postsCommentPage.getTotalPages(), 1);
        assertEquals(postsCommentPage.getNumber(), 0);
        List<PostsComment> postsComments = postsCommentPage.getContent();
        assertEquals(postsComments.get(0).getComment(), "test");
    }
}
