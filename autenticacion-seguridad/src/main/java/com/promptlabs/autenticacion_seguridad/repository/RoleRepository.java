package com.promptlabs.autenticacion_seguridad.repository;

import com.promptlabs.autenticacion_seguridad.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
    Optional<RoleEntity> findByRoleName(String roleName);
}
