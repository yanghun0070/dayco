package io.github.dayco.posts.ui;

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
import io.github.dayco.posts.ui.dto.PostsLikeDto;
import io.github.dayco.posts.ui.dto.PostsLikeMessageDto;
import io.github.dayco.posts.ui.dto.PostsMessageDto;

@Controller
public class PostsSocketController {

    @Autowired
    private UserClient userClient;

    private final PostsService postsService;
    private final MessageSourceAccessor messageSourceAccessor;

    private static final String POSTS_CREATED = "create";
    private static final String POSTS_EDITED = "edit";
    private static final String POSTS_DELETED = "delete";

    private static final String POSTS_LIKE_GETED = "likeGet";
    private static final String POSTS_LIKE_INCREASED = "likeIncrease";
    private static final String POSTS_LIKE_DECREASED = "likeDecrease";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public PostsSocketController(PostsService postsService,
                           MessageSourceAccessor messageSourceAccessor) {
        this.postsService = postsService;
        this.messageSourceAccessor = messageSourceAccessor;
    }

    @MessageMapping("/posts")
    @SendTo("/topic/posts")
    public PostsMessageDto message(PostsMessageDto postsMessageDto,
                            @Header("Authorization") String authorizationHeader) {
        User user = userClient.getCurrentUser(authorizationHeader);
        if(user == null) {
            throw new IllegalArgumentException("User doesn't Exist");
        }
        if(postsMessageDto == null) {
            throw new IllegalArgumentException("Posts Message doesn't Exist");
        }

        switch (postsMessageDto.getStatus()) {
            case POSTS_CREATED:
                return new PostsMessageDto(
                    postsService.save(postsMessageDto.getTitle(),
                                      postsMessageDto.getContent(),
                                      user.getUserId()), POSTS_CREATED);
            case POSTS_EDITED:
                return new PostsMessageDto(
                    postsService.update(postsMessageDto.getId(),
                                        postsMessageDto.getTitle(),
                                        postsMessageDto.getContent()), POSTS_EDITED);
            case POSTS_DELETED:
                postsService.delete(postsMessageDto.getId());
                PostsMessageDto postMessage = new PostsMessageDto();
                postMessage.setId(postsMessageDto.getId());
                postMessage.setAuthor(postsMessageDto.getAuthor());
                return postsMessageDto;
            default:
                throw new IllegalArgumentException("Posts Message Status Code = "  + postsMessageDto.getStatus());
        }
    }

    @MessageMapping("/posts/like")
    @SendTo("/topic/posts/like")
    public PostsLikeMessageDto increaseLikeMessage(PostsLikeMessageDto postsLikeMessage,
                                            @Header("Authorization") String authorizationHeader) {
        User user = userClient.getCurrentUser(authorizationHeader);
        if(user == null) {
            throw new IllegalArgumentException("User doesn't Exist");
        }

        if(postsLikeMessage == null) {
            throw new IllegalArgumentException("PostsLikeMessage doesn't Exist");
        }
        switch (postsLikeMessage.getStatus()) {
            case POSTS_LIKE_GETED:
                return new PostsLikeMessageDto(postsLikeMessage.getId(),
                                               user.getUserId(),
                                               postsService.getLikes(postsLikeMessage.getId()),
                                               POSTS_LIKE_GETED);
            case POSTS_LIKE_INCREASED:
                return new PostsLikeMessageDto(postsLikeMessage.getId(),
                                               user.getUserId(),
                                               postsService.increaseLike(postsLikeMessage.getId(),
                                                                         user.getUserId()),
                                               POSTS_LIKE_INCREASED);
            case POSTS_LIKE_DECREASED:
                return new PostsLikeMessageDto(postsLikeMessage.getId(),
                                               user.getUserId(),
                                               postsService.decreaseLike(postsLikeMessage.getId(),
                                                                         user.getUserId()),
                                               POSTS_LIKE_DECREASED);
            default:
                throw new IllegalArgumentException("PostsLike Message Type Status = "  + postsLikeMessage.getStatus());
        }
    }
}
