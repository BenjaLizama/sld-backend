package com.promptlabs.autenticacion_seguridad.controller;

import com.promptlabs.autenticacion_seguridad.dto.*;
import com.promptlabs.autenticacion_seguridad.service.impl.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterWrapper registerWrapper) {
        return ResponseEntity.ok(authService.register(registerWrapper));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginWrapper loginWrapper) {
        return ResponseEntity.ok(authService.login(loginWrapper));
    }

}
