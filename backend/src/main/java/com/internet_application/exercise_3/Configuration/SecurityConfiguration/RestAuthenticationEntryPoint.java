package com.internet_application.exercise_3.Configuration.SecurityConfiguration;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Overrides Spring common behavior to redirect unauthorized clients to the login page when they try to access a
 * secured resource.
 * Since this behavior doesn't make any sense for a REST Web Service, we make all client requests fail with a 401
 * UNAUTHORIZED status code.
 */
@Component
public final class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized");
    }
}
