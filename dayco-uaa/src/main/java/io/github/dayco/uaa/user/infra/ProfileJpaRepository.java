package io.github.dayco.uaa.user.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.dayco.uaa.user.domain.Profile;

public interface ProfileJpaRepository extends JpaRepository<Profile, String> {

}
