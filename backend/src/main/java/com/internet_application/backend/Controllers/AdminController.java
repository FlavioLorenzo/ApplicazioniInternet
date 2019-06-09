package com.internet_application.backend.Controllers;

import com.internet_application.backend.Configuration.SecurityConfiguration.JwtTokenProvider;
import com.internet_application.backend.Entities.UserEntity;
import com.internet_application.backend.PostBodies.ModifyRolePostBody;
import com.internet_application.backend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {
    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @GetMapping("/users")
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/users/{userID}")
    public ResponseEntity modifyUser(@PathVariable(value="userID") Long userId,
                                             @RequestBody ModifyRolePostBody mrpb) {
        userService.modifyRole(userId, mrpb.getLine(), mrpb.getRole());
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
}
