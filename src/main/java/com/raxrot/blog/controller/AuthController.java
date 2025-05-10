package com.raxrot.blog.controller;

import com.raxrot.blog.dto.JWTAuthResponse;
import com.raxrot.blog.dto.LoginDTO;
import com.raxrot.blog.dto.RegisterDTO;
import com.raxrot.blog.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Login user with username or email and password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<JWTAuthResponse> login(@Valid @RequestBody LoginDTO loginDTO) {
        log.info("POST /api/auth/login - Attempting login for: {}", loginDTO.getUsernameOrEmail());

        String token = authService.login(loginDTO);
        JWTAuthResponse jwtAuthResponse=new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        return ResponseEntity.ok(jwtAuthResponse);
    }

    @Operation(summary = "Register a new user account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registration successful"),
            @ApiResponse(responseCode = "400", description = "Validation failed or user already exists")
    })
    @PostMapping(value = {"/register","signup"})
    public ResponseEntity<String>register(@Valid @RequestBody RegisterDTO registerDTO) {
        log.info("POST /api/auth/register - Registering user: {}", registerDTO.getUsername());

        String response = authService.register(registerDTO);
        return ResponseEntity.ok(response);
    }
}
