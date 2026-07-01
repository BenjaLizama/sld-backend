package com.promptLabs.asistencia.dto;

import com.promptLabs.asistencia.enums.Status;

import java.time.LocalDate;
import java.util.UUID;

public record AttendanceResponseDto(
        LocalDate attendanceDate,
        Status attendanceStatus,
        UUID studentId,
        UUID attendanceId


) {
}
