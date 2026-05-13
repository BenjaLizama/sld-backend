package com.promptlabs.autenticacion_seguridad.service.impl;

import com.promptlabs.autenticacion_seguridad.repository.UserSessionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionCleanupServiceTest {

    @Mock
    private UserSessionRepository userSessionRepository;

    @InjectMocks
    private SessionCleanupService sessionCleanupService;

    @Test
    @DisplayName("Debería invocar la eliminación de sesiones expiradas usando el tiempo actual")
    void cleanExpiredTokens_ShouldInvokeRepository() {
        sessionCleanupService.cleanExpiredTokens();

        verify(userSessionRepository, times(1)).deleteByExpiryDateBefore(any(Instant.class));
    }

    @Test
    @DisplayName("Debería verificar que el Instant enviado no es futuro")
    void cleanExpiredTokens_VerifyTimeIsCoherent() {
        sessionCleanupService.cleanExpiredTokens();

        verify(userSessionRepository).deleteByExpiryDateBefore(argThat(instant ->
                instant.isBefore(Instant.now().plusMillis(100))
        ));
    }
}
