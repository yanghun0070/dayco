package com.ykgroup.dayco.uaa.auth.config;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class DefaultAccessDecisionManager implements AccessDecisionManager {

    public void decide(Authentication authentication, Object object,
                       Collection<ConfigAttribute> configAttributes) throws AccessDeniedException,
                                                                            InsufficientAuthenticationException {
        if (configAttributes == null ) {
            throw new AccessDeniedException("Sorry, you don't have this permission.");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(new Date()) + ":\t" + object.toString());

        for(ConfigAttribute ca: configAttributes){
            String needRole = ca.getAttribute();
            for(GrantedAuthority ga: authentication.getAuthorities()) {
                if(needRole.equals(ga.getAuthority())) {   // ga is user's role.
                    return;
                }
            }
        }
        throw new AccessDeniedException("Sorry, you don't have this permission.");
    }

    public boolean supports(ConfigAttribute arg0) {
        return true;
    }

    public boolean supports(Class<?> arg0) {
        return true;
    }
}
