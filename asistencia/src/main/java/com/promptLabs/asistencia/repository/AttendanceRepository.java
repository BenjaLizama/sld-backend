package com.promptLabs.asistencia.repository;

import com.promptLabs.asistencia.entity.Attendance;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    List<Attendance> findAllByStudentId(UUID studentId);


    Optional<Attendance> findByStudentIdAndAttendanceDate(UUID studentId, LocalDate attendanceDate);
}
