package com.promptLabs.ms_notas.dto;

import jakarta.annotation.Nullable;

import java.util.UUID;

public record GradeResponseDto(
        Double valor,
        String name,
        UUID id,
        UUID studentId,
        UUID teacherId
) {

}
