package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.UserEntity;
import com.internet_application.backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Needed to implement the login/authentication with Spring Security,
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    /**
     * This is the method that SHOULD load a user by his username. Just a small problem: we have the email.
     * Nevermind, let's hope it works and, if not, we'll all blame Marco for this ;)
     * @param email The email of the user to be loaded
     * @return The details related to the user
     * @throws UsernameNotFoundException The name says all... even though it's the email and not the username... silly Marco!
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException(email);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().getName()));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }
}
