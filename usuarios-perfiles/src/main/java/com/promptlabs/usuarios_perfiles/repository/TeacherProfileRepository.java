package com.promptlabs.usuarios_perfiles.repository;

import com.promptlabs.usuarios_perfiles.entity.TeacherProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, UUID> {
}
