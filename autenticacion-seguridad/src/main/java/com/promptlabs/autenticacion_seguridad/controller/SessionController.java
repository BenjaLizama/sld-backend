package com.promptlabs.autenticacion_seguridad.controller;

import com.promptlabs.autenticacion_seguridad.dto.AuthResponse;
import com.promptlabs.autenticacion_seguridad.dto.ChangePasswordRequest;
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
    public ResponseEntity<Void> deactivate() {
        sessionService.deactivateSelf();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        sessionService.changePassword(request);
        return ResponseEntity.noContent().build();
    }

    // extrae el token de header autorization y se lo pasa al service
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        sessionService.logout();
        return ResponseEntity.noContent().build();
    }

}
