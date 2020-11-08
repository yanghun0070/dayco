package io.github.dayco.posts.ui.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class PostsLikeMessageDto  extends PostsLikeDto {

    private String status; // 타입 (increase, decrease )

    public PostsLikeMessageDto(Long id, String userId, Long likeCount, String status) {
        super(id, userId, likeCount);
        this.status = status;
    }
}
