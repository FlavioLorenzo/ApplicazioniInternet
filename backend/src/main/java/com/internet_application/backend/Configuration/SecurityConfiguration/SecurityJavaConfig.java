package com.internet_application.backend.Configuration.SecurityConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {

    /*@Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;*/

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private CustomSavedRequestAwareAuthenticationSuccessHandler mySuccessHandler;

    private SimpleUrlAuthenticationFailureHandler myFailureHandler = new SimpleUrlAuthenticationFailureHandler();

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /* Disabled for development */
        http
            .csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint(restAuthenticationEntryPoint)
        .and()
            .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                .antMatchers("/login", "/register", "/confirm-account", "/recover", "/recover/**",
                        "/check-email", "/reservations/**", "/rides/**", "/availability/**", "/availability**", "/availabilities/**",
                        "/children**", "/children/**", "/child/**", "/token-info**", "/token-info/**", "/complete-registration**",
                        "/complete-registration/**", "/notifications**", "/notifications/**")
                    .permitAll()
                //.antMatchers("/reservations")
                    //.hasRole("USER")
                .antMatchers("/css/**")
                    .permitAll()
                .antMatchers("/ws**", "/ws/**")
                    .permitAll()
                    //.hasAnyRole("ADMIN", "SYS_ADMIN")
                /* Secure admin controller */
                .antMatchers("/users**", "users/**")
                    .hasAnyRole("SYS_ADMIN","ADMIN")
                .anyRequest().authenticated()
        .and()
            .apply(new JwtConfigurer(jwtTokenProvider));



        /*.and()
            .formLogin()
            .successHandler(mySuccessHandler)
            .failureHandler(myFailureHandler)
        .and()
            .logout()*/;
    }
}
