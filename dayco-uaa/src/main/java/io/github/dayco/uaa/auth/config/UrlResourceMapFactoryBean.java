package io.github.dayco.uaa.auth.config;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import io.github.dayco.uaa.manager.service.ManagerService;

public class UrlResourceMapFactoryBean
        implements FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>> {

    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMatcherToConfigAttrs;
    private ManagerService managerService;

    public UrlResourceMapFactoryBean(ManagerService managerService) {
        this.managerService = managerService;
    }

    @Override
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getObject() throws Exception {
        if(requestMatcherToConfigAttrs == null) {
            requestMatcherToConfigAttrs = managerService.findRoleResources();
        }
        return requestMatcherToConfigAttrs;
    }

    @Override
    public Class<?> getObjectType() {
        return LinkedHashMap.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
