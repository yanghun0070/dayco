package io.github.dayco.posts.infra;

import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class PostsRedisRepository {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;


    public static final String POSTS_LIKE = "POSTS_LIKE";

    public long increaseLikeCount(Long postsId, String userId) {
        return Optional.ofNullable(valueOps.increment(POSTS_LIKE + "_" + postsId))
                       .orElse(0L);
    }

    public long decreaseLikeCount(Long postsId, String userId) {
        return Optional.ofNullable(valueOps.decrement(POSTS_LIKE + "_" + postsId))
                       .filter(count -> count > 0).orElse(0L);
    }

    public long getLikeCount(Long postsId) {
        return Long.valueOf(Optional.ofNullable(valueOps.get(POSTS_LIKE + "_" + postsId))
                                    .orElse("0"));
    }
}
