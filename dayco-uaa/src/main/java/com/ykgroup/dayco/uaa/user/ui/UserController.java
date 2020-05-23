package com.ykgroup.dayco.uaa.user.ui;

import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ykgroup.dayco.uaa.auth.config.CurrentUser;
import com.ykgroup.dayco.uaa.user.application.UserService;
import com.ykgroup.dayco.uaa.user.domain.User;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{userId}")
    public Optional<User> getUser(@PathVariable String userId) {
        return userService.findByUserId(userId);
    }

    @GetMapping("me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser User user) {
        return userService.findByUserId(user.getUserId())
                          .orElseThrow(() -> new UsernameNotFoundException("Username " + user.getUserId() + " not found"));
    }
}

