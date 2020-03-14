package com.ykgroup.dayco.uaa.manager.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ykgroup.dayco.uaa.manager.domain.Resource;

public interface ResourceJpaRepository extends JpaRepository<Resource, Integer> {
}
