package com.ykgroup.dayco.uaa.user.application;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ykgroup.dayco.uaa.user.domain.User;
import com.ykgroup.dayco.uaa.manager.domain.UserAuthorization;
import com.ykgroup.dayco.uaa.user.infra.UserAuthorizationJpaRepository;
import com.ykgroup.dayco.uaa.user.infra.UserJpaRepository;

@Service
public class UserService {
    private UserJpaRepository userJpaRepository;
    private UserAuthorizationJpaRepository userAuthorizationJpaRepository;

    public UserService(UserJpaRepository userJpaRepository,
                       UserAuthorizationJpaRepository userAuthorizationJpaRepository) {
        this.userJpaRepository = userJpaRepository;
        this.userAuthorizationJpaRepository = userAuthorizationJpaRepository;
    }

    public Optional<User> findByUserId(String userId) {
        return userJpaRepository.findByUserId(userId);
    }

    public void join(User user) {
        User changedUser = new User(user.getUserId(), user.getPassword());
        changedUser.addUserAuthorization(new UserAuthorization(changedUser, "USER"));
        userJpaRepository.save(changedUser);
    }
}
