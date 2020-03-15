package com.ykgroup.dayco.uaa.auth.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class DefaultFilterInvocationMetadataSource implements FilterInvocationSecurityMetadataSource {

    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMatcherToConfigAttrs;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public DefaultFilterInvocationMetadataSource(LinkedHashMap<RequestMatcher,
            List<ConfigAttribute>> requestMatcherToConfigAttrs) {
        this.requestMatcherToConfigAttrs = requestMatcherToConfigAttrs;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        HttpServletRequest request = ((FilterInvocation) object).getRequest();
        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();
        String httpMethod = fi.getRequest().getMethod();
        logger.info("HTTP method: [" + httpMethod + "]. HTTP URL: [" + url + "].");

        List<ConfigAttribute> result = null;
        for (Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMatcherToConfigAttrs.entrySet()) {
            if (entry.getKey().matches(request)) {
                result = entry.getValue();
                logger.info("result:" + result);
                break;
            }
        }
        return result;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<>();
        for (Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMatcherToConfigAttrs.entrySet()) {
            allAttributes.addAll(entry.getValue());
        }
        return allAttributes;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}
