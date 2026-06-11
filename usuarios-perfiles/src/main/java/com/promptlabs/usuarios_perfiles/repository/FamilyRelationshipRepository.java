package com.promptlabs.usuarios_perfiles.repository;

import com.promptlabs.usuarios_perfiles.entity.FamilyRelationship;
import com.promptlabs.usuarios_perfiles.entity.ParentProfile;
import com.promptlabs.usuarios_perfiles.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FamilyRelationshipRepository extends JpaRepository<FamilyRelationship,Long> {
    boolean existsByParentProfileAndStudentProfile(ParentProfile parent, StudentProfile student);
    Optional<FamilyRelationship> findByParentProfileAndStudentProfile(ParentProfile parent, StudentProfile student);
    java.util.List<FamilyRelationship> findByStudentProfileUserRut(String studentRut);
}

