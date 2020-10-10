package io.github.dayco.posts.ui.dto;

import java.time.LocalDateTime;

import io.github.dayco.external.ui.vo.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostsCommentDto {
    private Long postsId; //게시글 ID
    private Long commentId; //댓글 ID
    private User author; //등록자
    private String comment; //댓글
    private LocalDateTime createTime; //생성 시간
    private LocalDateTime modifiedTime; //수정 시간

    public PostsCommentDto(Long postsId, Long commentId, User author, String comment,
                           LocalDateTime createTime, LocalDateTime modifiedTime) {
        this.postsId = postsId;
        this.commentId = commentId;
        this.author = author;
        this.comment = comment;
        this.createTime = createTime;
        this.modifiedTime = modifiedTime;
    }

}
