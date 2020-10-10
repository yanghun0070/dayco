package io.github.dayco.external.ui;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import io.github.dayco.external.ui.vo.User;

@FeignClient(name = "user-api", url = "${feign.user-api.url}")
public interface UserClient {

    @GetMapping("/user/me")
    public User getCurrentUser(@RequestHeader(value = "Authorization") String authorizationHeader);

    @GetMapping("/user/{userId}")
    public Optional<User> getUser(@PathVariable String userId);
}
