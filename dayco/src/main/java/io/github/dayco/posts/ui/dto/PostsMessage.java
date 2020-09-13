package io.github.dayco.posts.ui.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class PostsMessage {

    public PostsMessage() {}

    @Builder
    public PostsMessage(String type, Long postsId, String sender,
                        String title, String content) {
        this.type = type;
        this.postsId = postsId;
        this.sender = sender;
        this.title = title;
        this.content = content;
    }

    private String type; // 타입 (create, edit, delete )
    private Long postsId; // 게시판 번호
    private String sender; // 메시지 보낸사람
    private String title; //제목
    private String content; //내용

}
