package com.ykgroup.dayco.uaa.manager.application;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import com.ykgroup.dayco.uaa.auth.application.UaaUserDetailService;
import com.ykgroup.dayco.uaa.manager.domain.Authorization;
import com.ykgroup.dayco.uaa.manager.domain.Resource;
import com.ykgroup.dayco.uaa.manager.infra.AuthorizationJpaRepository;
import com.ykgroup.dayco.uaa.manager.infra.ResourceJpaRepository;
import com.ykgroup.dayco.uaa.manager.domain.UserAuthorization;
import com.ykgroup.dayco.uaa.user.infra.UserAuthorizationJpaRepository;

@Service
public class ManagerService {

    private ResourceJpaRepository resourceJpaRepository;
    private AuthorizationJpaRepository authorizationJpaRepository;
    private UserAuthorizationJpaRepository userAuthorizationJpaRepository;
    private UaaUserDetailService uaaUserDetailService;

    public ManagerService(ResourceJpaRepository resourceJpaRepository,
                          AuthorizationJpaRepository authorizationJpaRepository,
                          UserAuthorizationJpaRepository userAuthorizationJpaRepository,
                          UaaUserDetailService uaaUserDetailService) {
        this.resourceJpaRepository = resourceJpaRepository;
        this.authorizationJpaRepository = authorizationJpaRepository;
        this.userAuthorizationJpaRepository = userAuthorizationJpaRepository;
        this.uaaUserDetailService = uaaUserDetailService;
    }

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> findRoleResources() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMatchToConfigAttrs = new LinkedHashMap<>();
        List<Resource> resources = resourceJpaRepository.findAll();
        if (resources != null) {
            for (Resource resource : resources) {
                List<ConfigAttribute> roles = new LinkedList<>();
                for (Authorization role : resource.getAuthorizations()) {
                    roles.add(new SecurityConfig(role.getName()));
                }
                requestMatchToConfigAttrs.put(new AntPathRequestMatcher(resource.getName()), roles);
            }
        }
        return requestMatchToConfigAttrs;
    }

    public void createResources(List<Resource> resources)  {
        resourceJpaRepository.saveAll(resources);
    }

    public void createResource(Resource resource)  {
        resourceJpaRepository.save(resource);
    }

    public void createAuthorization(Authorization authorization) {
        authorizationJpaRepository.save(authorization);
    }

    public void createAuthorizations(List<Authorization> authorizations) {
        authorizationJpaRepository.saveAll(authorizations);
    }

    public void createUserAuthorization(UserAuthorization userAuthorization) {
        userAuthorizationJpaRepository.save(userAuthorization);
    }

    public List<UserAuthorization> selectUserAuthorizations() {
        return userAuthorizationJpaRepository.findAll();
    }
}
