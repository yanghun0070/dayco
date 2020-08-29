package io.github.dayco.uaa.auth.application;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import io.github.dayco.uaa.auth.config.JwtTokenProvider;
import io.github.dayco.uaa.auth.dto.SessionUser;
import io.github.dayco.uaa.auth.exception.InvalidJwtAuthenticationException;
import io.github.dayco.uaa.user.application.UserService;
import io.github.dayco.uaa.user.domain.User;

@Service
public class AuthenticationService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void signUp(SessionUser sessionUser, HttpServletResponse res) {
        try {
            userService.join(sessionUser.getUserId(), sessionUser.getPassword(), sessionUser.getEmail());
            String userName = sessionUser.getUserId();
            final Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, sessionUser.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            throw new InvalidJwtAuthenticationException("signup fail", e);
        }
    }

    public Map<String, String> signIn(SessionUser sessionUser, HttpSession session, HttpServletResponse res)  {
        try {
            String userName = sessionUser.getUserId();
            Optional<User> optionalUser = userService.findByUserId(userName);
            Authentication authentication;
            if(optionalUser.isEmpty()) {
                authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, sessionUser.getPassword()));
            } else {
                authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName,
                                                                                                        sessionUser.getPassword(),
                                                                                                        optionalUser.get().getAuthorities()));
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                                 SecurityContextHolder.getContext());
            logger.info("Successful authentication. Security context contains: "
                               + SecurityContextHolder.getContext().getAuthentication());

            String token = jwtTokenProvider.createToken(userName,
                                                        optionalUser
                                                                   .orElseThrow(() -> new UsernameNotFoundException("Username " + userName + " not found"))
                                                                   .getUserAuthorizations()
                                                                   .stream()
                                                                   .map(userAuthorization ->
                                                                                userAuthorization.getRoleName())
                                                                   .collect(Collectors.toList()));
            jwtTokenProvider.addTokenInHeader(token, res);
            Map<String, String> model = new HashMap<>();
            model.put("username", userName);
            model.put("token", token);
            return model;
        } catch (AuthenticationException e) {
            throw new InvalidJwtAuthenticationException("login fail", e);
        }
    }

    public Map<String, String> refresh(HttpServletRequest request) {
        String resolveToken = jwtTokenProvider.resolveToken(request);
        Map<String, String> model = new HashMap<>();
        model.put("username", jwtTokenProvider.getUsername(resolveToken));
        model.put("token", jwtTokenProvider.refreshToken(resolveToken));
        return model;
    }
}
