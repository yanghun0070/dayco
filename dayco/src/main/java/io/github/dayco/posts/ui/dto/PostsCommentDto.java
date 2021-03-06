package io.github.dayco.posts.ui.dto;

import java.time.LocalDateTime;

import io.github.dayco.external.ui.vo.User;
import io.github.dayco.posts.domain.PostsComment;
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
    private String createTime; //생성 시간 YYYY-MM-dd'T'HH:mm:ss
    private String modifiedTime; //수정 시간 YYYY-MM-dd'T'HH:mm:ss

    public PostsCommentDto(PostsComment postsComment, User author) {
        this.postsId = postsComment.getPosts().getId();
        this.commentId = postsComment.getId();
        this.author = author;
        this.comment = postsComment.getComment();
        this.createTime = postsComment.getCreatedDate().toString();
        this.modifiedTime = postsComment.getModifiedDate().toString();
    }

    public PostsCommentDto(Long postsId, Long commentId, User author, String comment,
                           LocalDateTime createTime, LocalDateTime modifiedTime) {
        this.postsId = postsId;
        this.commentId = commentId;
        this.author = author;
        this.comment = comment;
        this.createTime = createTime.toString();
        this.modifiedTime = modifiedTime.toString();
    }

}
