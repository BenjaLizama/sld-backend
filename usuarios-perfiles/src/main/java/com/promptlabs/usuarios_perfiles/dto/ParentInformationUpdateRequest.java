package com.promptlabs.usuarios_perfiles.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ParentInformationUpdateRequest(

        @NotBlank(message = "El nivel educacional es obligatorio")
        String educationLevel,

        @NotNull(message = "Debe indicar si es sostenedor o no")
        Boolean isSupporter
) {}
