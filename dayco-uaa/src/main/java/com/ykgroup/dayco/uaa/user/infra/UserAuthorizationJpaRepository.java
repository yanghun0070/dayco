package com.ykgroup.dayco.uaa.user.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ykgroup.dayco.uaa.manager.domain.UserAuthorization;

public interface UserAuthorizationJpaRepository extends JpaRepository<UserAuthorization, Integer> {
}
