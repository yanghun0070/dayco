package io.github.dayco.uaa.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.dayco.uaa.manager.domain.Resource;

public interface ResourceJpaRepository extends JpaRepository<Resource, Integer> {
}
