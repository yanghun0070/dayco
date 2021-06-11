package io.github.dayco.uaa.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.dayco.uaa.user.domain.User;
import io.github.dayco.uaa.manager.domain.UserAuthorization;
import io.github.dayco.uaa.user.repository.UserAuthorizationJpaRepository;
import io.github.dayco.uaa.user.repository.UserJpaRepository;

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

    public void join(String userId, String password, String email) {
        User changedUser = new User(userId, password);
        changedUser.setEmail(email);
        changedUser.addUserAuthorization(new UserAuthorization(changedUser, "USER"));
        userJpaRepository.save(changedUser);
    }
}
