package com.promptlabs.autenticacion_seguridad.controller;

import com.promptlabs.autenticacion_seguridad.dto.*;
import com.promptlabs.autenticacion_seguridad.service.impl.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Endpoints para el registro e inicio de sesión de usuarios")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Registrar nuevo usuario", description = "Crea un nuevo usuario en el sistema y emite un token JWT inicial.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de registro inválidos")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterWrapper registerWrapper) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registerWrapper));
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario con sus credenciales y retorna un token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginWrapper loginWrapper) {
        return ResponseEntity.ok(authService.login(loginWrapper));
    }

}
