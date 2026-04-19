package com.promptlabs.autenticacion_seguridad.repository;

import com.promptlabs.autenticacion_seguridad.entity.PrivilegeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrivilegeRepository extends JpaRepository<PrivilegeEntity, UUID> {
    Optional<PrivilegeEntity> findByName(String name);
    boolean existsByName(String name);
}
