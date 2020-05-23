package com.ykgroup.dayco.uaa.auth.ui;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ykgroup.dayco.uaa.auth.application.AuthenticationService;
import com.ykgroup.dayco.uaa.auth.dto.SessionUser;
import com.ykgroup.dayco.uaa.common.presentation.vo.GlobalMessage;
import com.ykgroup.dayco.uaa.common.presentation.vo.Result;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authService;
    private final MessageSourceAccessor messageSourceAccessor;

    public AuthenticationController(AuthenticationService authService,
                                    MessageSourceAccessor messageSourceAccessor) {
        this.authService = authService;
        this.messageSourceAccessor = messageSourceAccessor;
    }

    @PostMapping("signup")
    public Result<GlobalMessage> signUp(@RequestBody SessionUser sessionUser,
                                        HttpServletResponse response) {
        authService.signUp(sessionUser, response);
        GlobalMessage globalMessage = new GlobalMessage(HttpStatus.OK.value(),
                                                        messageSourceAccessor.getMessage(String.valueOf(HttpStatus.OK.value())));
        Result<GlobalMessage> result = new Result<>(globalMessage);
        result.add(
                linkTo(methodOn(AuthenticationController.class).signUp(sessionUser, response)).withSelfRel());
        return result;
    }

    @PostMapping("signin")
    public Result<Map<String, String>> signIn(@RequestBody SessionUser sessionUser,
                                              HttpSession session, HttpServletResponse response)  {
        Result<Map<String, String>> result = new Result<>(authService.signIn(sessionUser, session, response));
        result.add(
                linkTo(methodOn(AuthenticationController.class).signIn(sessionUser, session, response)).withSelfRel());
        return result;
    }

    @PostMapping("refresh")
    public Result<Map<String, String>> refresh(HttpServletRequest request) {
        Result<Map<String, String>> result = new Result<>(authService.refresh(request));
        result.add(
                linkTo(methodOn(AuthenticationController.class).refresh(request)).withSelfRel());
        return result;
    }
}
