package com.internet_application.exercise_3.Configuration.SecurityConfiguration;

import javax.naming.AuthenticationException;

public class InvalidJwtAuthenticationException extends AuthenticationException {
    public InvalidJwtAuthenticationException(String e) {
        super(e);
    }
}
