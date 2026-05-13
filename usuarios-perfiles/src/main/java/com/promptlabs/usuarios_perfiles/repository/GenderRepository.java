package com.promptlabs.usuarios_perfiles.repository;

import com.promptlabs.usuarios_perfiles.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenderRepository extends JpaRepository<Gender,Long> {
}
