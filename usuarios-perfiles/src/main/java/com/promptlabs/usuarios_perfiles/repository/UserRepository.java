package com.promptlabs.usuarios_perfiles.repository;

import com.promptlabs.usuarios_perfiles.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByRut(String rut);
}
