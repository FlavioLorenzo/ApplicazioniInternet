package com.internet_application.exercise_3.Controllers;

import com.internet_application.exercise_3.Configuration.SecurityConfiguration.JwtTokenProvider;

import com.internet_application.exercise_3.Entities.UserEntity;
import com.internet_application.exercise_3.PostBodies.LoginPostBody;
import com.internet_application.exercise_3.PostBodies.RecoverNewPwdPostBody;
import com.internet_application.exercise_3.PostBodies.RecoverRequestPostBody;
import com.internet_application.exercise_3.PostBodies.RegistrationPostBody;
import com.internet_application.exercise_3.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerErrorException;

import javax.validation.Valid;
import java.util.*;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity login(@RequestBody @Valid LoginPostBody lpg) {
        try {
            String mail = lpg.getEmail();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(mail, lpg.getPassword()));
            UserEntity u = this.userService.findByEmail(mail);
            if (u == null)
                throw new UsernameNotFoundException("Username " + mail + "not found");
            if (!u.getEnabled())
                throw new Exception();
            // TODO add support for multiple roles
            List<String> roles = new ArrayList<>();
            roles.add(u.getRole().getName());
            String token = jwtTokenProvider.createToken(mail, new ArrayList<String>(/*u.getRole().getName()*/)/*u.getRoles()*/);
            Map<Object, Object> model = new HashMap<>();
            model.put("mail", mail);
            model.put("token", token);
            return ResponseEntity.ok(model);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username/password");
        }
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity register(@RequestBody @Valid RegistrationPostBody rpb) {
        try {
            userService.register(rpb.getEmail(), rpb.getPassword(), rpb.getConfirmPassword(), rpb.getFirstName(), rpb.getLastName());
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        } catch (Exception e) {
            if(e instanceof ResponseStatusException){
                throw e;
            }else{
               // throw new BadCredentialsException("Invalid username/password");
            }
            throw e;
        }
    }

    @GetMapping("/confirm-account")
    @ResponseBody
    public ResponseEntity confirmAccount(@RequestParam String token) {
        userService.confirmAccount(token);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @PostMapping("/recover")
    @ResponseBody
    public ResponseEntity recoverAccount(@RequestBody @Valid RecoverRequestPostBody rrpb) {
        userService.recoverAccount(rrpb.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body("Email with link to restore password sent");
    }

    @PostMapping(value="/recover/{randomUUID}")
    @ResponseBody
    public ResponseEntity restorePassword(@PathVariable(value="randomUUID") String token, @RequestBody @Valid RecoverNewPwdPostBody rrpb) {
        userService.restorePassword(rrpb.getPassword(), rrpb.getConfirmPassword(), token);
        return ResponseEntity.status(HttpStatus.OK).body("Password correctly restored");
    }
}

