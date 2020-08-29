package io.github.dayco.posts.infra;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.github.dayco.posts.domain.Posts;

public interface PostsJpaRepository extends JpaRepository<Posts, Long> {

    @Query("select p from Posts p order by p.id desc")
    List<Posts> findAllDesc();
}
