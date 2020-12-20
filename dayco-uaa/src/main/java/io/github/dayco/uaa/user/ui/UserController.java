package io.github.dayco.uaa.user.ui;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.dayco.uaa.auth.config.CurrentUser;
import io.github.dayco.uaa.user.application.ProfileService;
import io.github.dayco.uaa.user.application.UserService;
import io.github.dayco.uaa.user.domain.User;
import io.github.dayco.uaa.user.ui.dto.UserDto;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final ProfileService profileService;

    public UserController(UserService userService,
                          ProfileService profileService) {
        this.userService = userService;
        this.profileService = profileService;
    }

    @GetMapping("{userId}")
    public Optional<UserDto> getUser(@PathVariable String userId) throws Exception {
        Optional<User> userOpt = userService.findByUserId(userId);
        /**
         * 유저 정보를 얻어 올 때, Expire 시간이 7 일이므로, Profile URL 을 갱신시킨다.
         * @todo 추후에 Redis 로 7일 지나기 전에 Profile URL 변경시키는 형태로 변경
         */
        profileService.changeProfileUrl(userId);
        return userOpt.map(
                user ->
                {
                    return UserDto.builder()
                                  .userId(user.getUserId())
                                  .email(user.getEmail().isEmpty() ? null: user.getEmail().get().getEmail())
                                  .age(user.getAge().orElse(null))
                                  .gender(user.getGender().orElse(null))
                                  .picture(user.getPicture().orElse(null))
                                  .createTime(user.getCreateTime())
                                  .modifyTime(user.getModifyTime())
                                  .build();
                }
        );
    }

    @GetMapping("me")
    @PreAuthorize("hasRole('USER')")
    public UserDto getCurrentUser(@CurrentUser User user) throws Exception {
        Optional<User> userOpt = userService.findByUserId(user.getUserId());
        /**
         * 유저 정보를 얻어 올 때, Expire 시간이 7 일이므로, Profile URL 을 갱신시킨다.
         * @todo 추후에 Redis 로 7일 지나기 전에 Profile URL 변경시키는 형태로 변경
         */
        profileService.changeProfileUrl(userOpt, user.getUserId());
        return userOpt.map(u ->
                {
                    return UserDto.builder()
                                  .userId(u.getUserId())
                                  .email(u.getEmail().isEmpty() ? null: u.getEmail().get().getEmail())
                                  .age(u.getAge().orElse(null))
                                  .gender(u.getGender().orElse(null))
                                  .picture(u.getPicture().orElse(null))
                                  .createTime(u.getCreateTime())
                                  .modifyTime(u.getModifyTime())
                                  .build();
                }
        ).orElseThrow(() -> new UsernameNotFoundException("Username " + user.getUserId() + " not found"));
    }
}

