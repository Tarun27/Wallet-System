package com.tarun.userService.controller;

import com.tarun.userService.dto.LoginRequest;
import com.tarun.userService.dto.LoginResponse;
import com.tarun.userService.dto.RegisterRequest;
import com.tarun.userService.dto.RegisterResponse;
import com.tarun.userService.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        RegisterResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @SecurityRequirement(name = "bearerAuth")
    
    @GetMapping("/users/me")
    public ResponseEntity<String> getCurrentUser(Authentication auth) {
        return ResponseEntity.ok("Logged in as: " + auth.getName());
    }


}
