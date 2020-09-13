package io.github.dayco.posts.ui;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import io.github.dayco.external.ui.UserClient;
import io.github.dayco.external.ui.vo.User;
import io.github.dayco.posts.application.PostsService;
import io.github.dayco.posts.domain.Posts;
import io.github.dayco.posts.ui.dto.PostsMessage;
import io.github.dayco.posts.ui.dto.PostsSaveRequestDto;
import io.github.dayco.posts.ui.dto.PostsUpdateRequestDto;

@Controller
public class PostsSocketController {

    @Autowired
    private UserClient userClient;

    private final PostsService postsService;
    private final MessageSourceAccessor messageSourceAccessor;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public PostsSocketController(PostsService postsService,
                           MessageSourceAccessor messageSourceAccessor) {
        this.postsService = postsService;
        this.messageSourceAccessor = messageSourceAccessor;
    }


    @MessageMapping("/dayco-websocket")
    @SendTo("/topic/posts")
    public Posts message(PostsMessage postsMessage,
                         @Header("Authorization") String authorizationHeader) {
        User user = userClient.getCurrentUser(authorizationHeader);
        if(user == null) {
            throw new IllegalArgumentException("User doesn't Exist");
        }
        postsMessage.setSender(user.getUserId());

        if(postsMessage == null) {
            throw new IllegalArgumentException("PostMessage doesn't Exist");
        }
        Posts posts = null;
        switch (postsMessage.getType()) {
            case "create":
                //String title, String content, String author
                posts = postsService.save(new PostsSaveRequestDto(
                        postsMessage.getTitle(),
                        postsMessage.getContent(),
                        postsMessage.getSender()));
                break;
            case "edit":
                posts = postsService.update(postsMessage.getPostsId(),
                                    new PostsUpdateRequestDto(postsMessage.getTitle(),
                                                              postsMessage.getContent(),
                                                              postsMessage.getSender()));
                break;
            case "delete":
                postsService.delete(postsMessage.getPostsId());
                posts = new Posts();
                posts.setId(postsMessage.getPostsId());
                posts.setAuthor(postsMessage.getSender());
                break;
        }
        return posts;
    }
}
