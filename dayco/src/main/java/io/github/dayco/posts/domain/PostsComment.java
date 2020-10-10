package io.github.dayco.posts.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(exclude = "posts")
@NoArgsConstructor
@Entity
public class PostsComment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "posts_comment_id")
    private Long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "posts_id")
    private Posts posts;
    private String author;
    private String comment;
    private LocalDateTime modifiedDate;
    private LocalDateTime createdDate;

    @Builder
    public PostsComment(Posts posts, String author, String comment) {
        this.posts = posts;
        this.comment = comment;
        this.author = author;
        this.modifiedDate = LocalDateTime.now();
        this.createdDate = LocalDateTime.now();
    }

    /**
     * 댓글을 수정한다.
     * @param commentsId 댓글 ID
     * @param comment 댓글
     */
    public void update(Long commentsId, String comment) {
        this.id = commentsId;
        this.comment = comment;
        this.modifiedDate = LocalDateTime.now();
    }
}