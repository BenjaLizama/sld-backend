package com.promptlabs.autenticacion_seguridad.repository;

import com.promptlabs.autenticacion_seguridad.entity.CredentialEntity;
import com.promptlabs.autenticacion_seguridad.entity.SessionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, UUID> {

    Optional<SessionEntity> findByRefreshTokenHash(String refreshTokenHash);

    // Encuentra una sesión específica de un usuario en un dispositivo concreto
    Optional<SessionEntity> findByCredentialAndDeviceId(CredentialEntity credential, String deviceId);

    @Modifying
    @Transactional
    void deleteByCredential(CredentialEntity credential);

    @Modifying
    @Transactional
    @Query("UPDATE SessionEntity s SET s.isActive = false WHERE s.credential.id = :credentialId")
    void revokeAllByCredentialId(UUID credentialId);

    @Modifying
    @Transactional
    void deleteByExpiryDateBefore(Instant now);

}
