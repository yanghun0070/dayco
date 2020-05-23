package com.ykgroup.dayco.uaa.auth.application;

import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ykgroup.dayco.uaa.user.domain.User;
import com.ykgroup.dayco.uaa.user.infra.UserJpaRepository;

@Service("uaaUserDetailsService")
public class UaaUserDetailService implements UserDetailsService {

    private UserJpaRepository userJpaRepository;

    public UaaUserDetailService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userJpaRepository.findByUserId(username).orElseThrow(
                () -> new UsernameNotFoundException("Username: " + username + " not found"));
         org.springframework.security.core.userdetails.User.withUsername(user.getUserId())
                   .password(user.getPassword())
                   .roles(
                           String.join(",", user.getUserAuthorizations()
                                                .stream().map(role -> role.getRoleName())
                                                .collect(Collectors.toList()))
                   )
                   .build();
        return  user;
    }
}
