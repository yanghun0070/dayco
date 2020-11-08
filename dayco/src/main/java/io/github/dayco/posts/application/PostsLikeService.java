package io.github.dayco.posts.application;

import org.springframework.stereotype.Service;

import io.github.dayco.posts.infra.PostsRedisRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostsLikeService {

    private final PostsRedisRepository postsRedisRepository;

    public long increaseLike(Long id, String userId) {
        return postsRedisRepository.increaseLikeCount(id, userId);
    }

    public long decreaseLike(Long id, String userId) {
        return postsRedisRepository.decreaseLikeCount(id, userId);
    }

    public long getLikes(Long id) {
        return postsRedisRepository.getLikeCount(id);
    }

}
