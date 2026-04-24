package com.promptlabs.autenticacion_seguridad.service.impl;

import com.promptlabs.autenticacion_seguridad.repository.UserSessionRepository;
import com.promptlabs.autenticacion_seguridad.service.ITokenCleanupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

// si el microservicio escala a más instancias, sería bueno poner sehdlock para que
// no hayan ejecuciones concurrentes del cleanup

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionCleanupService implements ITokenCleanupService {

    private final UserSessionRepository userSessionRepository;

    @Scheduled(cron = "${app.cleanup.session.cron}")
    @Transactional
    @Override
    public void cleanExpiredTokens() {
        int deleted = userSessionRepository.deleteByExpiryDateBefore(Instant.now());
        log.info("[SESSION-CLEANUP] Sesiones expiradas eliminadas: {}", deleted);
    }
}