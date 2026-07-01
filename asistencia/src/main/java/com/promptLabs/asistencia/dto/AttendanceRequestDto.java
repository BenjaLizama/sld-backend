package com.promptLabs.asistencia.dto;

import com.promptLabs.asistencia.enums.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record AttendanceRequestDto(
        @PastOrPresent(message = "la asistencia tiene que ser del pasado o presente")
        @NotNull
        LocalDate attendanceDate,
        @NotNull(message = "no se puede agreagr asistencia sin estado: PRESENT, LATER, ABSENT ")
        Status attendanceStatus


) {
}
