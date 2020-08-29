package io.github.dayco.uaa.user.infra;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.dayco.uaa.user.domain.User;

public interface UserJpaRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserId(String userId);

    Optional<User> findByEmailEmail(String email);
}
