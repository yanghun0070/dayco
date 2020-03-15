package com.ykgroup.dayco.uaa.auth.ui;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ykgroup.dayco.uaa.auth.application.AuthenticationService;
import com.ykgroup.dayco.uaa.auth.domain.AuthenticationRequest;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("signin")
    public ResponseEntity signIn(@RequestBody AuthenticationRequest data, HttpServletResponse response) throws Exception {
        return authService.signIn(data, response);
    }

}
