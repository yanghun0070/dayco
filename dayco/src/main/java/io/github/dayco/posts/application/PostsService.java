package io.github.dayco.posts.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.dayco.external.ui.UserClient;
import io.github.dayco.external.ui.vo.User;
import io.github.dayco.posts.domain.Posts;
import io.github.dayco.posts.domain.PostsComment;
import io.github.dayco.posts.infra.PostsJpaRepository;
import io.github.dayco.posts.ui.dto.PostsCommentDto;
import io.github.dayco.posts.ui.dto.PostsLikeDto;
import io.github.dayco.posts.ui.dto.PostsListResponseDto;
import io.github.dayco.posts.ui.dto.PostsResponseDto;
import io.github.dayco.posts.ui.dto.PostsSaveRequestDto;
import io.github.dayco.posts.ui.dto.PostsUpdateRequestDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final UserClient userClient;
    private final PostsJpaRepository postsJpaRepository;
    private final PostsLikeService postsLikeService;
    private final PostsCommentService postsCommentService;

    @Transactional
    public Posts save(PostsSaveRequestDto requestDto) {
        return postsJpaRepository.save(
                new Posts(requestDto.getTitle(),
                          requestDto.getContent(),
                          requestDto.getAuthor()));
    }

    @Transactional
    public Posts update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsJpaRepository.findById(id)
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                "There are not posts. id = " + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());
        return posts;
    }

    public PostsResponseDto find(Long id) {
        Posts entity = postsJpaRepository.findById(id)
                                         .orElseThrow(() -> new IllegalArgumentException(
                                                 "There are not posts. id = " + id));

        return new PostsResponseDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsJpaRepository.findById(id)
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                "There are not posts. id = " + id));
        postsJpaRepository.delete(posts);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAll() {
        return postsJpaRepository.findAllDesc().stream()
                         .map(PostsListResponseDto::new)
                         .collect(Collectors.toList());
    }

    public PostsLikeDto increaseLike(Long id, String userId) {
        return postsLikeService.increaseLike(id, userId);
    }

    public PostsLikeDto decreaseLike(Long id, String userId) {
        return postsLikeService.decreaseLike(id, userId);
    }

    public PostsLikeDto getLikes(Long id) {
        return postsLikeService.getLikes(id);
    }

    public List<PostsCommentDto> getPostsComments(List<Long> postsIds, int page, int rowNum) {
        List<PostsComment> postsComments = postsCommentService.getPostsComments(postsIds, page, rowNum);

        List<PostsCommentDto> postsCommentDtos = postsComments.stream().map(postsComment -> {
            return new PostsCommentDto(
                    postsComment.getPosts().getId(),
                    postsComment.getId(),
                    userClient.getUser(postsComment.getAuthor()).get(),
                    postsComment.getComment(),
                    postsComment.getCreatedDate(),
                    postsComment.getModifiedDate());
       }).collect(Collectors.toList());
        return postsCommentDtos;
    }

    public Page<PostsCommentDto> getPageOfComments(Long postsId, int page, int rowNum) {
        Page<PostsComment> pageOfComments = postsCommentService.getPostsCommentsByPaging(postsId, page, rowNum);
        Page<PostsCommentDto> postsCommentDtos = pageOfComments.map(postsComment -> {
            return new PostsCommentDto(
                    postsComment.getPosts().getId(),
                    postsComment.getId(),
                    userClient.getUser(postsComment.getAuthor()).get(),
                    postsComment.getComment(),
                    postsComment.getCreatedDate(),
                    postsComment.getModifiedDate());
        });
        return postsCommentDtos;
    }

    /**
     * 게시글 댓글 생성
     * @param postsId Posts ID
     * @param author 등록자
     * @param comment 댓글
     * @return 등록되어진 댓글
     */
    public PostsCommentDto createComment(Long postsId, User author, String comment) {
        return postsCommentService.createComment(postsId, author, comment);
    }

    /**
     * 게시글 댓글 수정
     * @param commentId 댓글 ID
     * @param author 등록자
     * @param comment 댓글
     * @return 수정되어진 댓글
     */
    public PostsCommentDto editComment(Long commentId, User author, String comment) {
        PostsComment postsComment = postsCommentService.updateComment(commentId, comment);
        return new PostsCommentDto(
                postsComment.getPosts().getId(),
                postsComment.getId(),
                author,
                postsComment.getComment(),
                postsComment.getCreatedDate(),
                postsComment.getModifiedDate()
        );
    }

    /**
     * 게시글 댓글 삭제
     * @param commentId 댓글 ID
     * @return 삭제된 댓글 ID
     */
    public Long deleteComment(Long commentId) {
        postsCommentService.deleteComment(commentId);
        return commentId;
    }

}
