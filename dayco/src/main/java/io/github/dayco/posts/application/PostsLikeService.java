package io.github.dayco.posts.application;

import org.springframework.stereotype.Service;

import io.github.dayco.posts.infra.PostsRedisRepository;
import io.github.dayco.posts.ui.dto.PostsLikeDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostsLikeService {

    private final PostsRedisRepository postsRedisRepository;

    public PostsLikeDto increaseLike(Long id, String userId) {
        return new PostsLikeDto(id, userId,
                                postsRedisRepository.increaseLikeCount(id, userId));
    }

    public PostsLikeDto decreaseLike(Long id, String userId) {
        return new PostsLikeDto(id, userId,
                                postsRedisRepository.decreaseLikeCount(id, userId));
    }

    public PostsLikeDto getLikes(Long id) {
        return new PostsLikeDto(id, postsRedisRepository.getLikeCount(id));
    }

}
