package io.github.dayco.posts.ui;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.dayco.common.presentation.vo.GlobalMessage;
import io.github.dayco.common.presentation.vo.Result;
import io.github.dayco.external.ui.UserClient;
import io.github.dayco.external.ui.vo.User;
import io.github.dayco.posts.application.PostsLikeService;
import io.github.dayco.posts.application.PostsService;
import io.github.dayco.posts.domain.Posts;
import io.github.dayco.posts.domain.PostsComment;
import io.github.dayco.posts.ui.dto.PostsCommentDto;
import io.github.dayco.posts.ui.dto.PostsDto;
import io.github.dayco.posts.ui.dto.PostsLikeDto;

@RestController
@RequestMapping("/posts")
public class PostsController {

    @Autowired
    private UserClient userClient;

    private final PostsService postsService;
    private final PostsLikeService postsLikeService;

    private final MessageSourceAccessor messageSourceAccessor;

    public PostsController(PostsService postsService,
                           PostsLikeService postsLikeService,
                           MessageSourceAccessor messageSourceAccessor) {
        this.postsService = postsService;
        this.postsLikeService = postsLikeService;
        this.messageSourceAccessor = messageSourceAccessor;
    }

    /**
     * Posts 생성한다.
     * @param postsDto Posts 정보
     * @param authorizationHeader 인증 Header
     * @return Posts 생성 정보
     */
    @PostMapping
    public Posts create(@ModelAttribute PostsDto postsDto,
                        @RequestHeader(value = "Authorization") String authorizationHeader) throws Exception {
        User user = userClient.getCurrentUser(authorizationHeader);
        if(user == null) {
            throw new IllegalArgumentException("User doesn't Exist");
        }
        postsDto.setAuthor(user.getUserId());
        return postsService.save(postsDto);
    }

    /**
     * Posts 를 변경한다.
     * @param id Posts ID
     * @param postsDto Posts 정보
     * @param authorizationHeader 인증 Header
     * @return Posts 수정된 정보
     */
    @PutMapping("/{id}")
    public Posts update(@PathVariable Long id,
                        @ModelAttribute PostsDto postsDto,
                        @RequestHeader(value = "Authorization") String authorizationHeader) throws Exception {
        User user = userClient.getCurrentUser(authorizationHeader);
        if(user == null) {
            throw new IllegalArgumentException("User doesn't Exist");
        }
        // 작성자와 글 수정자와 다를 경우,
        if(!postsDto.getAuthor().equals(user.getUserId())) {
            throw new IllegalArgumentException("Different from author and post modifier");
        }
        return postsService.update(id, postsDto.getTitle(), postsDto.getContent(),
                                   postsDto.getFileName(), postsDto.getFileBase64());
    }

    /**
     * Posts 를 조회한다.
     * @param id Posts ID
     * @return Posts 정보
     */
    @GetMapping("/{id}")
    public PostsDto get(@PathVariable Long id) {
        return postsService.findWithCommentsAndLikeCnt(id);
    }

    /**
     * Posts 를 삭제한다.
     * @param id Posts ID
     */
    @DeleteMapping("/{id}")
    public Result<GlobalMessage> delete(@PathVariable Long id) {
        postsService.delete(id);
        GlobalMessage globalMessage = new GlobalMessage(HttpStatus.OK.value(),
                                                        messageSourceAccessor.getMessage(String.valueOf(HttpStatus.OK.value())));
        Result<GlobalMessage> result = new Result<>(globalMessage);
        result.add(
                linkTo(methodOn(PostsController.class).delete(id)).withSelfRel());
        return result;
    }

    @GetMapping("/all")
    public List<PostsDto> all() throws Exception {
        return postsService.findAll();
    }


    /**
     * Posts Like 건수를 증가시킨다.
     * @param id Posts ID
     * @param authorizationHeader
     * @return Like 건수
     */
    @PostMapping("/like/increase/{id}")
    public PostsLikeDto increaseLike(@PathVariable Long id,
                                     @RequestHeader(value = "Authorization") String authorizationHeader) {
        User user = userClient.getCurrentUser(authorizationHeader);
        if(user == null) {
            throw new IllegalArgumentException("User doesn't Exist");
        }
        return new PostsLikeDto(id,
                                user.getUserId(),
                                postsService.increaseLike(id, user.getUserId()));
    }

    /**
     * Posts Like 건수를 감소시킨다.
     * @param id Posts ID
     * @param authorizationHeader
     * @return Like 건수
     */
    @PostMapping("/like/decrease/{id}")
    public PostsLikeDto decreaseLike(@PathVariable Long id,
                                     @RequestHeader(value = "Authorization") String authorizationHeader) {
        User user = userClient.getCurrentUser(authorizationHeader);
        if(user == null) {
            throw new IllegalArgumentException("User doesn't Exist");
        }

        return new PostsLikeDto(id,
                                user.getUserId(),
                                postsService.decreaseLike(id,
                                                          user.getUserId()));
    }

    @GetMapping("/likes/{id}")
    public PostsLikeDto getLikes(@PathVariable Long id) {
        return new PostsLikeDto(id, postsService.getLikes(id));
    }

    /**
     * 댓글을 조회
     * @param postsIds Posts ID 목록
     * @param page 현재 페이지
     * @param rowNum 보여줄 건수
     * @return
     */
    @GetMapping("/comments")
    public List<PostsCommentDto> getPostsComments(
            @RequestParam(value="postsIds") List<Long> postsIds, @RequestParam int page, @RequestParam int rowNum) {
        return postsService.getPostsComments(postsIds, page, rowNum);
    }

    /**
     * 페이징 처리된 댓글을 조회
     * @param postsId Posts ID
     * @param page 현재 페이지
     * @param rowNum 보여줄 건수
     * @return
     */
    @GetMapping("/comments/page")
    public Page<PostsCommentDto> getPageOfComments(
            @RequestParam(required = false) Long postsId, @RequestParam int page, @RequestParam int rowNum) {
        return postsService.getPageOfComments(postsId, page, rowNum);
    }


    /**
     * 게시글 댓글 생성
     * @param postsId  Posts ID
     * @param authorizationHeader
     * @param comment 댓글
     * @return 등록되어진 게시글 댓글
     */
    @PostMapping("/comment")
    public PostsCommentDto createComment(
            @RequestParam Long postsId,
            @RequestParam String comment,
            @RequestHeader(value = "Authorization") String authorizationHeader) {
        User user = userClient.getCurrentUser(authorizationHeader);
        if(user == null) {
            throw new IllegalArgumentException("User doesn't Exist");
        }
        return postsService.createComment(postsId, user, comment);
    }

    /**
     * 게시글 댓글을 수정한다.
     * @param commentId 댓글 ID
     * @param comment 댓글
     * @param authorizationHeader
     * @return 수정되어진 댓글
     */
    @PutMapping("/comment/{commentId}")
    public PostsCommentDto editComment(@PathVariable Long commentId,
                                       @RequestHeader(value = "Authorization") String authorizationHeader,
                                       @RequestParam String comment) {
        User user = userClient.getCurrentUser(authorizationHeader);
        if(user == null) {
            throw new IllegalArgumentException("User doesn't Exist");
        }
        return postsService.editComment(commentId, user, comment);
    }

    /**
     * 게시글 댓글을 삭제한다.
     * @param commentId 댓글 ID
     * @param authorizationHeader
     * @return 삭제되어진 댓글 정보
     */
    @DeleteMapping("/comment/{commentId}")
    public PostsComment deleteComment(@PathVariable Long commentId,
                                      @RequestHeader(value = "Authorization") String authorizationHeader) {
        User user = userClient.getCurrentUser(authorizationHeader);
        if(user == null) {
            throw new IllegalArgumentException("User doesn't Exist");
        }
        return postsService.deleteComment(commentId);
    }
}
