package com.promptlabs.autenticacion_seguridad.service.impl;

import com.promptlabs.autenticacion_seguridad.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountCleanupService {

    private final CredentialRepository credentialRepository;

    @Scheduled(cron = "0 0 3 * * ?") // 3 am todos los días
    @Transactional
    public void deleteInactiveAccounts() {
        Instant cutoff = Instant.now().minus(30, ChronoUnit.DAYS);
        int deleted = credentialRepository.deleteByIsActiveFalseAndDeactivatedAtBefore(cutoff);
        log.info("([ACCOUNT-CLEANUP] cuentas eliminadas por inactividad de 30 días: {}", deleted);
    }

}
