package io.github.dayco.posts.ui.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsLikeDto {

    private Long id;
    private String userId;
    private Long likeCount;

    public PostsLikeDto(Long id, Long likeCount) {
        this.id = id;
        this.likeCount = likeCount;
    }

    public PostsLikeDto(Long id, String userId, Long likeCount) {
        this.id = id;
        this.userId = userId;
        this.likeCount = likeCount;
    }
}
