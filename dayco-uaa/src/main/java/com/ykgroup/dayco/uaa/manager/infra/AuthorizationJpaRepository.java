package com.ykgroup.dayco.uaa.manager.infra;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ykgroup.dayco.uaa.manager.domain.Authorization;

public interface AuthorizationJpaRepository extends JpaRepository<Authorization, Integer> {

    public List<Authorization> findByNameIn(List<Long> roleNames);
}
