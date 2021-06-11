package io.github.dayco.uaa.user.ui;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.dayco.uaa.auth.config.CurrentUser;
import io.github.dayco.uaa.auth.ui.AuthenticationController;
import io.github.dayco.uaa.common.presentation.vo.GlobalMessage;
import io.github.dayco.uaa.common.presentation.vo.Result;
import io.github.dayco.uaa.user.application.ProfileService;
import io.github.dayco.uaa.user.application.UserService;
import io.github.dayco.uaa.user.domain.User;
import io.github.dayco.uaa.user.ui.dto.ProfileDto;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProfileService profileService;
    private final UserService userService;
    private final MessageSourceAccessor messageSourceAccessor;

    public ProfileController(ProfileService profileService,
                             UserService userService,
                             MessageSourceAccessor messageSourceAccessor) {
        this.profileService = profileService;
        this.userService = userService;
        this.messageSourceAccessor = messageSourceAccessor;
    }

    /**
     * Profile 을 변경한다.
     * @param fileBase64 이미지 바이너리를 읽을 수 있는 스트링 형태로 바꾼 BASE64
     * @param fileName 파일 명
     * @param user
     * @throws Exception
     */
    @PostMapping(value = "change")
    public Result<GlobalMessage> change(
                            @RequestParam String fileBase64,
                            @RequestParam String fileName,
                            @CurrentUser User user) throws Exception {

        logger.info("fileBase64 : {}, fileName: {}", fileBase64, fileName);

        profileService.upload(fileBase64, fileName, user);

        GlobalMessage globalMessage = new GlobalMessage(HttpStatus.OK.value(),
                                                        messageSourceAccessor.getMessage(String.valueOf(HttpStatus.OK.value())));
        Result<GlobalMessage> result = new Result<>(globalMessage);
        result.add(
                linkTo(methodOn(ProfileController.class).change(fileBase64, fileName, user)).withSelfRel());
        return result;
    }
}
