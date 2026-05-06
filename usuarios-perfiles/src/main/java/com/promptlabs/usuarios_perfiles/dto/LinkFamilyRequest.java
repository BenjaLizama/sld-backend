package com.promptlabs.usuarios_perfiles.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record LinkFamilyRequest(
        @NotNull(message = "El ID del apoderado es obligatorio")
        UUID parentId,

        @NotNull(message = "El ID del estudiante es obligatorio")
        UUID studentId,

        @NotNull(message = "El ID del tipo de parentesco es obligatorio")
        Long parentTypeId // (O UUID, dependiendo de cómo creaste ParentType)
) {}