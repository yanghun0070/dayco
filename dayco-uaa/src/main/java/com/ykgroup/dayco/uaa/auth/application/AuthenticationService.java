package com.ykgroup.dayco.uaa.auth.application;

import static org.springframework.http.ResponseEntity.ok;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.ykgroup.dayco.uaa.auth.domain.AuthenticationRequest;
import com.ykgroup.dayco.uaa.auth.config.JwtTokenProvider;
import com.ykgroup.dayco.uaa.user.application.UserService;
import com.ykgroup.dayco.uaa.user.domain.User;

@Service
public class AuthenticationService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;
    private UaaUserDetailService uaaUserDetailService;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            UserService userService, JwtTokenProvider jwtTokenProvider,
            UaaUserDetailService uaaUserDetailService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.uaaUserDetailService = uaaUserDetailService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public ResponseEntity signIn(@RequestBody AuthenticationRequest authRequest, HttpServletResponse res)  {
        try {
            String userName = authRequest.getUsername();
            Optional<User> optionalUser = userService.findByUserId(userName);
            Authentication authentication = null;
            if(optionalUser.isEmpty()) {
                authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, authRequest.getPassword()));
            } else {
                authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName,
                                                                                                        authRequest.getPassword(),
                                                                                                        optionalUser.get().getAuthorities()));
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
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
            return ok(model);
        }catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }
}
