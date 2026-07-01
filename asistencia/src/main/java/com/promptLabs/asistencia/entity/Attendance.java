package com.promptLabs.asistencia.entity;

import com.promptLabs.asistencia.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "attendance", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id","attendance_date"}))
@Data
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Column(name = "attendance_date",nullable = false)
    private LocalDate attendanceDate;

    @Column
    private LocalTime attendanceTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

}
