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
import io.github.dayco.uaa.user.service.ProfileService;
import io.github.dayco.uaa.user.service.UserService;
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
        if(userOpt.isPresent()) {
            profileService.changeImageUrl(userOpt.get().getProfile(), userId);
        }
        return (userOpt.isPresent()) ? Optional.of(modelToDto(userOpt.get())) : Optional.empty();
    }

    @GetMapping("me")
    @PreAuthorize("hasRole('USER')")
    public UserDto getCurrentUser(@CurrentUser User user) throws Exception {
        User currentUser = userService.findByUserId(user.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Username " + user.getUserId() + " not found"));

        /**
         * 유저 정보를 얻어 올 때, Expire 시간이 7 일이므로, Profile URL 을 갱신시킨다.
         * @todo 추후에 Redis 로 7일 지나기 전에 Profile URL 변경시키는 형태로 변경
         */
        profileService.changeImageUrl(currentUser.getProfile(), user.getUserId());
        return modelToDto(currentUser);
    }

    private UserDto modelToDto(User user) {
        return UserDto.builder()
                      .userId(user.getUserId())
                      .email(user.getEmail().map(e -> e.getEmail()))
                      .age(user.getAge())
                      .gender(user.getGender())
                      .profileImageUrl(user.getProfile().map(
                              profile -> profile.getImageUrl()))
                      .createTime(user.getCreateTime())
                      .updateTime(user.getUpdateTime())
                      .build();
    }
}

