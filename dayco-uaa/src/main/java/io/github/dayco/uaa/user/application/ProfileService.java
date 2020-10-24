package io.github.dayco.uaa.user.application;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.dayco.uaa.user.domain.User;
import io.github.dayco.uaa.user.infra.UserJpaRepository;

@Service
public class ProfileService {

    private final UserJpaRepository userJpaRepository;

    public ProfileService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Transactional
    public User change(String email, Optional<String> password, Optional<String> fileSavedPath) {
        User user = userJpaRepository.findByEmailEmail(email)
                         .orElseThrow(() -> new IllegalArgumentException(
                                               "There are not email = " + email));
        return user.update(email, password, fileSavedPath);
    }
}
