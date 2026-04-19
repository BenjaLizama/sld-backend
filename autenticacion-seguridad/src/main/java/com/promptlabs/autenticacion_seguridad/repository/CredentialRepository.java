package com.promptlabs.autenticacion_seguridad.repository;

import com.promptlabs.autenticacion_seguridad.entity.CredentialEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CredentialRepository extends JpaRepository<CredentialEntity, UUID> {

    @Query("SELECT c FROM CredentialEntity c " +
            "LEFT JOIN FETCH c.roleList r " +
            "LEFT JOIN FETCH r.privileges " +
            "WHERE c.email = :email")
    Optional<CredentialEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    int deleteByIsActiveFalseAndDeactivatedAtBefore(Instant cutoff);

}
