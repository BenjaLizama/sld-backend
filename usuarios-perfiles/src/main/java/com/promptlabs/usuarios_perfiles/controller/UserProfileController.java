package com.promptlabs.usuarios_perfiles.controller;

import com.promptlabs.usuarios_perfiles.dto.UserProfileCompletionRequest;
import com.promptlabs.usuarios_perfiles.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;

    @PutMapping("/{userId}/complete-profile")
    public ResponseEntity<String> completeProfile(
            @PathVariable UUID userId,
            @Valid @RequestBody UserProfileCompletionRequest request
    ) {
        try {
            userService.completarPerfilBase(userId, request);
            return ResponseEntity.ok("Perfil base actualizado con éxito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/{userId}/profile-update")
    public ResponseEntity<String> updateProfile(
            @RequestHeader("Authorization") String token,
            @PathVariable UUID userId,
            @RequestBody Map<String, Object> body
    ) {
        System.out.println("llamada correcta");

        userService.actualizarPerfilEspecifico(userId, token, body);

        return ResponseEntity.ok("Procesado correctamente");
    }
}