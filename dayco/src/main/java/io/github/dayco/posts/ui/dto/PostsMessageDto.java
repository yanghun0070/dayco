package io.github.dayco.posts.ui.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.github.dayco.posts.domain.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class PostsMessageDto extends PostsDto {
    private String status; // 타입 (create, edit, delete )

    public PostsMessageDto(Posts posts, String status) {
        super(posts);
        this.status = status;
    }

    @JsonIgnore
    public PostsLikeDto getPostsLike() {
        return postsLike;
    }

    @JsonIgnore
    public List<PostsCommentDto> getPostsComments() {
        return postsComments;
    }

}
