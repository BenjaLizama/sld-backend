package com.promptlabs.autenticacion_seguridad.service.impl;

import com.promptlabs.autenticacion_seguridad.repository.SessionsRepository;
import com.promptlabs.autenticacion_seguridad.service.ITokenCleanupService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SessionCleanupService implements ITokenCleanupService {

    private final SessionsRepository sessionsRepository;

    @Scheduled(cron = "0 0 6 * * ?") // Se ejecuta a las 6 de la mañana cada día.
    @Override
    public void cleanExpiredTokens() {
        sessionsRepository.deleteByExpiryDateBefore(Instant.now());
    }
}
