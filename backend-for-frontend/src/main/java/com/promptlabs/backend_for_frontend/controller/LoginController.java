package com.promptlabs.backend_for_frontend.controller;

import com.promptlabs.backend_for_frontend.dto.AuthLoginResponse;
import com.promptlabs.backend_for_frontend.dto.LoginRequestDTO;
import com.promptlabs.backend_for_frontend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Endpoints para el inicio de sesión y gestión de tokens.")
public class LoginController {
    private final AuthService authService;

    @Operation(summary = "Iniciar sesión", description = "Valida las credenciales del usuario y devuelve los tokens de acceso.")
    @ApiResponse(responseCode = "200", description = "Login exitoso")
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(
            @Parameter(description = "ID único del dispositivo")
            @RequestHeader(value = "X-Device-ID", required = false) String deviceId,
            @RequestBody LoginRequestDTO request) {

        return ResponseEntity.ok(
                authService.login(request,deviceId));
    }
}
