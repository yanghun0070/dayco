package com.ykgroup.dayco.uaa.user.infra;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ykgroup.dayco.uaa.user.domain.User;

public interface UserJpaRepository extends JpaRepository<User, Integer> {

    public Optional<User> findByUserId(String userId);
}
