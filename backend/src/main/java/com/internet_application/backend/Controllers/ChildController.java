package com.internet_application.backend.Controllers;

import com.internet_application.backend.Configuration.SecurityConfiguration.JwtTokenProvider;
import com.internet_application.backend.Entities.UserEntity;
import com.internet_application.backend.PostBodies.*;
import com.internet_application.backend.Services.ChildService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin()
@RestController
public class ChildController {
    @Autowired
    ChildService childService;
}

