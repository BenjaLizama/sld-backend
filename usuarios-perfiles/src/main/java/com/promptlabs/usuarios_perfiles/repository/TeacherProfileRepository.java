package com.promptlabs.usuarios_perfiles.repository;

import com.promptlabs.usuarios_perfiles.entity.TeacherProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, UUID> {
}
