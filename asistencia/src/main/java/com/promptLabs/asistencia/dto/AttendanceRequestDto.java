package com.promptLabs.asistencia.dto;

import com.promptLabs.asistencia.enums.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.UUID;

public record AttendanceRequestDto(
        @PastOrPresent
        @NotNull
        LocalDate attendanceDate,
        @NotNull
        Status attendanceStatus


) {
}
