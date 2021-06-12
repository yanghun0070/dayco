package io.github.dayco.uaa.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.dayco.uaa.manager.domain.UserAuthorization;

public interface UserAuthorizationJpaRepository extends JpaRepository<UserAuthorization, Integer> {
}
