package com.ykgroup.dayco.posts.ui;

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

import com.ykgroup.dayco.common.presentation.vo.GlobalMessage;
import com.ykgroup.dayco.common.presentation.vo.Result;
import com.ykgroup.dayco.external.ui.UserClient;
import com.ykgroup.dayco.external.ui.vo.User;
import com.ykgroup.dayco.posts.application.PostsService;
import com.ykgroup.dayco.posts.domain.Posts;
import com.ykgroup.dayco.posts.ui.dto.PostsListResponseDto;
import com.ykgroup.dayco.posts.ui.dto.PostsResponseDto;
import com.ykgroup.dayco.posts.ui.dto.PostsSaveRequestDto;
import com.ykgroup.dayco.posts.ui.dto.PostsUpdateRequestDto;


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
        if(requestDto.getAuthor().equals(user.getUserId())) {
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
}
