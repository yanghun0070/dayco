package io.github.dayco.posts.ui;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.dayco.common.presentation.vo.GlobalMessage;
import io.github.dayco.common.presentation.vo.Result;
import io.github.dayco.external.ui.UserClient;
import io.github.dayco.external.ui.vo.User;
import io.github.dayco.posts.application.PostsService;
import io.github.dayco.posts.domain.Posts;
import io.github.dayco.posts.ui.dto.PostsLikeDto;
import io.github.dayco.posts.ui.dto.PostsListResponseDto;
import io.github.dayco.posts.ui.dto.PostsResponseDto;
import io.github.dayco.posts.ui.dto.PostsSaveRequestDto;
import io.github.dayco.posts.ui.dto.PostsUpdateRequestDto;

@RestController
@RequestMapping("/posts")
public class PostsController {

    @Autowired
    private UserClient userClient;

    private final PostsService postsService;
    private final MessageSourceAccessor messageSourceAccessor;

    public PostsController(PostsService postsService,
                           MessageSourceAccessor messageSourceAccessor) {
        this.postsService = postsService;
        this.messageSourceAccessor = messageSourceAccessor;
    }

    @PostMapping
    public Posts create(@RequestBody PostsSaveRequestDto requestDto,
                        @RequestHeader(value = "Authorization") String authorizationHeader) {
        User user = userClient.getCurrentUser(authorizationHeader);
        if(user == null) {
            throw new IllegalArgumentException("User doesn't Exist");
        }
        requestDto.setAuthor(user.getUserId());
        return postsService.save(requestDto);
    }

    @PutMapping("/{id}")
    public Posts update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto,
                                        @RequestHeader(value = "Authorization") String authorizationHeader) {
        User user = userClient.getCurrentUser(authorizationHeader);
        if(user == null) {
            throw new IllegalArgumentException("User doesn't Exist");
        }
        // 작성자와 글 수정자와 다를 경우,
        if(!requestDto.getAuthor().equals(user.getUserId())) {
            throw new IllegalArgumentException("Different from author and post modifier");
        }
        return postsService.update(id, requestDto);
    }

    @GetMapping("/{id}")
    public PostsResponseDto get(@PathVariable Long id) {
        return postsService.find(id);
    }

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
    public List<PostsListResponseDto> all() {
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
        return postsService.increaseLike(id, user.getUserId());
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
        return postsService.decreaseLike(id, user.getUserId());
    }

    @GetMapping("/likes/{id}")
    public PostsLikeDto getLikes(@PathVariable Long id) {
        return postsService.getLikes(id);
    }
}
