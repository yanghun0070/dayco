package io.github.dayco.posts.ui.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class PostsLikeMessage {

    public PostsLikeMessage() {}

    @Builder
    public PostsLikeMessage(String type, Long postsId,
                            String sender) {
        this.type = type;
        this.postsId = postsId;
        this.sender = sender;
    }

    private String type; // 타입 (increase, decrease )
    private Long postsId; // 게시판 번호
    private String sender;

}
