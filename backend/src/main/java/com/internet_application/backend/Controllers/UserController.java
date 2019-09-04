package com.internet_application.backend.Controllers;

import com.internet_application.backend.Configuration.SecurityConfiguration.JwtTokenProvider;

import com.internet_application.backend.Entities.BusLineEntity;
import com.internet_application.backend.Entities.RoleEntity;
import com.internet_application.backend.Entities.UserEntity;
import com.internet_application.backend.PostBodies.*;
import com.internet_application.backend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@CrossOrigin()
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
            model.put("id", u.getId());
            model.put("mail", mail);
            model.put("role", u.getRole());
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
            userService.register(rpb.getEmail(), rpb.getFirstName(), rpb.getLastName(), rpb.getRoleId());
            return ResponseEntity.status(HttpStatus.OK).body("");
        } catch (Exception e) {
            if(e instanceof ResponseStatusException){
                throw e;
            }else{
               // throw new BadCredentialsException("Invalid username/password");
            }
            throw e;
        }
    }

    @GetMapping("/token-info/{token}")
    @ResponseBody
    public UserEntity confirmAccount(@PathVariable(value="token") String token)
        throws ResponseStatusException {
        return userService.getAccountConfirmationInfo(token);
    }

    @PostMapping("/complete-registration/{token}")
    @ResponseBody
    public void completeAccount(@PathVariable(value="token") String token,
                                @RequestBody @Valid CompleteUserPostBody completeUserPostBody)
        throws ResponseStatusException {
        userService.completeAccount(
                token,
                completeUserPostBody.getPassword(),
                completeUserPostBody.getConfirmPassword(),
                completeUserPostBody.getPhone()
        );
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

    @PostMapping("/check-email")
    @ResponseBody
    public ResponseEntity checkEmail(@RequestBody @Valid CheckEmailPostBody cepb) {
        userService.checkEmail(cepb.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @PostMapping("/user/add-admin/{userId}/{lineId}")
    public UserEntity addAdminRoleOfLineToUser(
            @PathVariable(value="lineId") Long lineId,
            @PathVariable(value="userId") Long userId
            ) {
        return userService.addAdminRoleOfLineToUser(lineId, userId);
    }

    @PostMapping("/user/remove-admin/{userId}/{lineId}")
    public UserEntity removeAdminRoleOfLineFromUser(
            @PathVariable(value="lineId") Long lineId,
            @PathVariable(value="userId") Long userId
    ) {
        return userService.removeAdminRoleOfLineFromUser(lineId, userId);
    }

    @GetMapping("/user/administered-line/{userId}")
    public List<BusLineEntity> getAdministeredLineOfUser(@PathVariable(value="userId") Long userId) {
        return userService.getAdministeredLineOfUser(userId);
    }


    @GetMapping("/user/principal")
    public UserEntity getUserName(Principal principal)
    {
        if (principal == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return userService.getUserFromEmail(principal.getName());
    }
}

