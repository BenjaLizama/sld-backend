package com.promptlabs.autenticacion_seguridad.controller;

import com.promptlabs.autenticacion_seguridad.dto.AuthResponse;
import com.promptlabs.autenticacion_seguridad.dto.RefreshTokenWrapper;
import com.promptlabs.autenticacion_seguridad.service.impl.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/session")
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenWrapper refreshTokenWrapper) {
        return ResponseEntity.ok(sessionService.refreshToken(refreshTokenWrapper));
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<?> deactivate() {
        sessionService.deactivateSelf();
        return ResponseEntity.ok("Cuenta desactivada con exito!");
    }

    // TODO: ELIMINAR ESTE CONTROLADOR, ES SOLO PARA PRUEBAS DE ROLES O PRIVILEGIOS.
    @GetMapping("/me")
    public ResponseEntity<?> getMyActiveSession() {
        return ResponseEntity.ok("Lista de sesiones");
    }

}
