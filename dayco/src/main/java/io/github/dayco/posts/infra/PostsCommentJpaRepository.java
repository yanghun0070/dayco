package io.github.dayco.posts.infra;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.dayco.posts.domain.PostsComment;

public interface PostsCommentJpaRepository extends JpaRepository<PostsComment, Long> {

    public List<PostsComment> findByPostsId(Long postsId);

    public Page<PostsComment> findByPostsIdOrderByIdDesc(Long postsId, Pageable pageable);
}
