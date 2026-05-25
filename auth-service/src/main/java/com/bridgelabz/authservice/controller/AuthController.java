// AuthController.java

package com.bridgelabz.authservice.controller;

import com.bridgelabz.authservice.dto.*;
import com.bridgelabz.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerUser(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(
                authService.registerUser(request)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> loginUser(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                authService.loginUser(request)
        );
    }
}