package com.promptlabs.usuarios_perfiles.repository;

import com.promptlabs.usuarios_perfiles.entity.ParentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParentProfileRepository extends JpaRepository<ParentProfile, UUID> {
}
