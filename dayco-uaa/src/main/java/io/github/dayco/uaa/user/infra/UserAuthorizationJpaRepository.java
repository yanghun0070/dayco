package io.github.dayco.uaa.user.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.dayco.uaa.manager.domain.UserAuthorization;

public interface UserAuthorizationJpaRepository extends JpaRepository<UserAuthorization, Integer> {
}
