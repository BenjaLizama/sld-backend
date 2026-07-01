package com.promptLabs.ms_notas.repository;

import com.promptLabs.ms_notas.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GradeRepository extends JpaRepository<Grade, UUID> {
    List<Grade> findByStudentId(UUID StudentId);

}
