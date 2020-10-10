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
import io.github.dayco.uaa.user.application.UserService;
import io.github.dayco.uaa.user.domain.User;
import io.github.dayco.uaa.user.ui.dto.UserDto;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{userId}")
    public Optional<UserDto> getUser(@PathVariable String userId) {
        Optional<User> userOpt = userService.findByUserId(userId);
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
    public UserDto getCurrentUser(@CurrentUser User user) {
        Optional<User> userOpt = userService.findByUserId(user.getUserId());
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

