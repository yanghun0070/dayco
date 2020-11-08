package io.github.dayco.posts.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.dayco.external.ui.vo.User;
import io.github.dayco.posts.domain.Posts;
import io.github.dayco.posts.domain.PostsComment;
import io.github.dayco.posts.infra.PostsCommentJpaRepository;
import io.github.dayco.posts.ui.dto.PostsCommentDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostsCommentService {

    private final PostsCommentJpaRepository postsCommentJpaRepository;

    /**
     * Posts 댓글 전체 조회
     * @param postsId Posts ID
     * @return
     */
    public List<PostsComment> getPostsCommentsByPostsId(Long postsId) {
        return postsCommentJpaRepository.findByPostsId(postsId);
    }

    @Transactional
    public Optional<PostsComment> getPostsComment(Long commentId) {
        return postsCommentJpaRepository.findById(commentId);
    }

    /**
     * 페이징으로 Posts 댓글 목록 조회
     * @param postsIds Posts ID 목록
     * @param page 현재 페이지
     * @param rowNum 페이징 번호
     * @return Posts 댓글 목록
     */
    @Transactional
    public List<PostsComment> getPostsComments(List<Long> postsIds, int page, int rowNum) {
        PageRequest pageRequest = PageRequest.of(page, rowNum);
        pageRequest.getSortOr(Sort.by(Sort.Direction.DESC, "id"));

        return postsIds.stream().map(postsId -> {
            Page<PostsComment> pageOfComments =  postsCommentJpaRepository
                    .findByPostsIdOrderByIdDesc(postsId, PageRequest.of(page, rowNum));
            return pageOfComments.getContent();
        }).flatMap(List::stream).collect(Collectors.toList());
    }

    @Transactional
    public Page<PostsComment> getPostsCommentsByPaging(Long postsId, int page, int rowNum) {
        PageRequest pageRequest = PageRequest.of(page, rowNum);
        pageRequest.getSortOr(Sort.by(Sort.Direction.DESC, "id"));
        return postsCommentJpaRepository.findByPostsIdOrderByIdDesc(postsId, PageRequest.of(page, rowNum));
    }

    /**
     * 게시글 댓글 생성
     * @param postsId Posts ID
     * @param author 등록자
     * @param comment 댓글
     * @return 등록되어진 댓글
     */
    @Transactional
    public PostsCommentDto createComment(Long postsId, User author, String comment) {
        Posts posts = new Posts();
        posts.setId(postsId);
        PostsComment postsComment = postsCommentJpaRepository
                .save(new PostsComment(
                        posts,
                        author.getUserId(),
                        comment));
        return new PostsCommentDto(postsComment.getPosts().getId(),
                                   postsComment.getId(),
                                   author,
                                   postsComment.getComment(),
                                   postsComment.getCreatedDate(),
                                   postsComment.getModifiedDate());
    }

    @Transactional
    public PostsComment updateComment(Long id, String comment) {
        PostsComment postsComment = postsCommentJpaRepository.findById(id)
                                 .orElseThrow(() -> new IllegalArgumentException(
                                         "There are not posts comment. id = " + id));

        postsComment.update(id, comment);
        return postsComment;
    }

    @Transactional
    public void deleteComment(Long commentId) {
        postsCommentJpaRepository.deleteById(commentId);
    }
}
