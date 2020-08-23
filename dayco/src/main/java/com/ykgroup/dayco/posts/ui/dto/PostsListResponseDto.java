package com.ykgroup.dayco.posts.ui.dto;

import java.time.LocalDateTime;

import com.ykgroup.dayco.posts.domain.Posts;

import lombok.Getter;

@Getter
public class PostsListResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime modifiedDate;

    public PostsListResponseDto(Posts posts) {
        this.id = posts.getId();
        this.title = posts.getTitle();
        this.content = posts.getContent();
        this.author = posts.getAuthor();
        this.modifiedDate = posts.getModifiedDate();
    }
}
