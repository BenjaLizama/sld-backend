package com.promptlabs.autenticacion_seguridad.repository;

import com.promptlabs.autenticacion_seguridad.entity.CredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CredentialRepository extends JpaRepository<CredentialEntity, UUID> {
    Optional<CredentialEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
