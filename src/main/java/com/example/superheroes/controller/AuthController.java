package com.example.superheroes.controller;

import com.example.superheroes.dto.LoginDto;
import com.example.superheroes.dto.UserDto;
import com.example.superheroes.model.User;
import com.example.superheroes.service.JwtUtilService;
import com.example.superheroes.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("api/v1/auth")
@Validated
@Slf4j
public class AuthController {

    final private AuthenticationManager authenticationManager;
    final private UserDetailsService customUserDetailsService;
    final private JwtUtilService jwtUtilService;
    final private UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserDetailsService customUserDetailsService, JwtUtilService jwtUtilService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtilService = jwtUtilService;
        this.userService = userService;
    }

    @PostMapping("/signup")
    @Operation(summary = "Register a user with userDto object")
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto, HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = userService.createUser(userDto);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error with user registration");
        }
        return ResponseEntity.created(URI.create(request.getRequestURI() + "/" + user.getId())).build();
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Login a user to obtain the jwt")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("login with user: {}", loginDto.username());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.username(),
                            loginDto.password()));
        } catch (AuthenticationException e) {
            Map<String, String> error = new HashMap<>();
            error.put("Error", "Unauthorized Access");
            throw new ResponseStatusException(FORBIDDEN, error.toString(), e);
        }

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(
                loginDto.username());

        response.setHeader("access_token", jwtUtilService.generateToken(userDetails));
        response.setHeader("refresh_token", jwtUtilService.refreshToken(userDetails));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/refresh")
    @Operation(summary = "Refresh the jwt", security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<?> refreshToken(@Parameter(hidden = true) @RequestHeader(name = "Authorization") String authorization, HttpServletRequest request, HttpServletResponse response) throws
            IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            Map<String, String> error = new HashMap<>();
            error.put("Error", "Unauthorized Access");
            throw new ResponseStatusException(FORBIDDEN, error.toString());
        }
        try {
            String token = authorizationHeader.substring("Bearer ".length());
            String username = jwtUtilService.extractSubject(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if (jwtUtilService.validateToken(token, userDetails)) {
                response.setHeader("access_token", jwtUtilService.generateToken(userDetails));
                response.setHeader("refresh_token", token);
            }
        } catch (Exception exception) {
            log.info("Incorrect refresh token: {}", authorizationHeader.substring("Bearer ".length()));
            Map<String, String> error = new HashMap<>();
            error.put("Error", "Unauthorized Access");
            throw new ResponseStatusException(FORBIDDEN, error.toString(), exception);
        }

        return ResponseEntity.ok().build();
    }
}

