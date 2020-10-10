package io.github.dayco.posts.ui.dto;

import io.github.dayco.posts.domain.Posts;

import lombok.Getter;

@Getter
public class PostsResponseDto {

    private Long id; //Posts ID
    private String title; //제목
    private String content; //내용
    private String author; //등록자
    private String modifiedDate; //수정 시간 YYYY-MM-dd'T'HH:mm:ss
    private String createdDate; //생성 시간 YYYY-MM-dd'T'HH:mm:ss

    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.modifiedDate = entity.getModifiedDate().toString();
        this.createdDate = entity.getCreatedDate().toString();
    }
}
