package com.promptlabs.usuarios_perfiles.controller;

import com.promptlabs.usuarios_perfiles.dto.AuthRegistrationRequest;
import com.promptlabs.usuarios_perfiles.dto.UserProfileCompletionRequest;
import com.promptlabs.usuarios_perfiles.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/internal/users")
@RequiredArgsConstructor
public class UserInternalController {

    private final UserService userService;

    @PostMapping("/initialize")
    public ResponseEntity<String> initializeUser(@RequestBody AuthRegistrationRequest request) {
        try {
            userService.crearUserCascaron(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario inicializado correctamente");
        } catch (Exception e) {
            // Un poco de logging básico para saber qué falló
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    @PutMapping("/{userId}/complete-profile")
    public ResponseEntity<String> completeProfile(
            @PathVariable UUID userId,
            @Valid @RequestBody UserProfileCompletionRequest request // 🛡️ @Valid es crucial aquí
    ) {
        try {
            userService.completarPerfilBase(userId, request);
            return ResponseEntity.ok("Perfil base actualizado con éxito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}