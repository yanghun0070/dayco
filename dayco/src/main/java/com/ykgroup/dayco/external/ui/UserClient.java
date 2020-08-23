package com.ykgroup.dayco.external.ui;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.ykgroup.dayco.external.ui.vo.User;

@FeignClient(name = "user-api", url = "${feign.user-api.url}")
public interface UserClient {

    @GetMapping("/user/me")
    public User getCurrentUser(@RequestHeader(value = "Authorization") String authorizationHeader);
}
