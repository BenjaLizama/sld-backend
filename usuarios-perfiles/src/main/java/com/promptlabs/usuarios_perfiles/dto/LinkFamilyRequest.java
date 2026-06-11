package com.promptlabs.usuarios_perfiles.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record LinkFamilyRequest(
        @Pattern(
                regexp = "^(\\d{1,2}\\.?\\d{3}\\.?\\d{3}-?[0-9kK]{1})$",
                message = "El RUT del apoderado no tiene un formato válido (ej: 12345678-9 o 12.345.678-K)"
        )
        @NotNull(message = "El rut del apoderado es obligatorio")
        String parentId,

        @Pattern(
                regexp = "^(\\d{1,2}\\.?\\d{3}\\.?\\d{3}-?[0-9kK]{1})$",
                message = "El RUT del apoderado no tiene un formato válido (ej: 12345678-9 o 12.345.678-K)"
        )
        @NotNull(message = "El rut del estudiante es obligatorio")
        String studentId,

        @NotNull(message = "El ID del tipo de parentesco es obligatorio")
        Long parentTypeId
) {}