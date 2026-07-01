package com.promptLabs.ms_notas.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;


public record GradeUpdateDto(

        @DecimalMax(value = "7.0", message = "la nota no puede ser mayor a 7.0")
        @DecimalMin(value = "1.0",message = "la nota no puede ser menor a 1.0")
        Double value,
        @Size(min = 1, max = 70)
        String name

) {
}
