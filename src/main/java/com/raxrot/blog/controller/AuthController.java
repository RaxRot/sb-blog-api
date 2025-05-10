package com.raxrot.blog.controller;

import com.raxrot.blog.dto.LoginDTO;
import com.raxrot.blog.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = {"/login","/signin"})
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO) {
       String response = authService.login(loginDTO);
       return ResponseEntity.ok(response);
    }
}
