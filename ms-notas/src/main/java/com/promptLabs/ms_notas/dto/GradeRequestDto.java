package com.promptLabs.ms_notas.dto;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record GradeRequestDto(

        @DecimalMax(message = "la nota maxina es 7.0", value = "7.0")
        @DecimalMin(message = "la nota minima es 1.0", value = "1.0")
        double value,

        @NotNull
        UUID studentId,

        @NotNull
        UUID teacherId,

        @Size(min = 3, max = 70)
        @NotNull
        @NotBlank
        String name
) {
}
