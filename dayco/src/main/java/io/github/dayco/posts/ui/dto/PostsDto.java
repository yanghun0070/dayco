package io.github.dayco.posts.ui.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import io.github.dayco.posts.domain.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostsDto {
    private Long id; //Posts 식별번호
    private String title; //제목
    private String content; //내용
    private String author; // 등록자
    private String modifiedDate; //수정 날짜
    private String createdDate; //생성 날짜
    private Optional<String> fileBase64; //File Base64
    private Optional<String> fileName; //파일명
    private Optional<String> fileSavedUrl;
    protected PostsLikeDto postsLike; //Like
    protected List<PostsCommentDto> postsComments; //댓글

    public PostsDto(Posts posts) {
        this.id = posts.getId();
        this.title = posts.getTitle();
        this.content = posts.getContent();
        this.author = posts.getAuthor();
        this.fileSavedUrl = Optional.ofNullable(posts.getFileSavedUrl());
        this.modifiedDate = posts.getModifiedDate().toString();
        this.createdDate = posts.getCreatedDate().toString();
    }

    public PostsDto(String title, String content, String author,
                    LocalDateTime modifiedDate, LocalDateTime createdDate) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.modifiedDate = modifiedDate.toString();
        this.createdDate = createdDate.toString();
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate.toString();
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate.toString();
    }

}
