package com.ykgroup.dayco.uaa.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidJwtAuthenticationException extends AuthenticationException {
    public InvalidJwtAuthenticationException(String msg) {
        super(msg);
    }

    public InvalidJwtAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }
}
