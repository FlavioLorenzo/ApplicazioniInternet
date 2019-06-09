package com.internet_application.backend.Configuration.SecurityConfiguration;

import javax.naming.AuthenticationException;

public class InvalidJwtAuthenticationException extends AuthenticationException {
    public InvalidJwtAuthenticationException(String e) {
        super(e);
    }
}
