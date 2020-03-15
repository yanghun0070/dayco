package com.ykgroup.dayco.uaa.user.ui;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ykgroup.dayco.uaa.user.application.UserService;
import com.ykgroup.dayco.uaa.user.domain.User;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("join")
    public void join(@RequestBody User user) {
        userService.join(user);
    }

    @GetMapping("{userId}")
    public Optional<User> getUser(@PathVariable String userId) {
        return userService.findByUserId(userId);
    }
}
