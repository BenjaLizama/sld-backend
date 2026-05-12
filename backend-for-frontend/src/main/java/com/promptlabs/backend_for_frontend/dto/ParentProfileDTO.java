package com.promptlabs.backend_for_frontend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ParentProfileDTO(

        @NotBlank(message = "El nivel educacional es obligatorio")
        String educationLevel,

        @NotNull(message = "Debe indicar si es sostenedor o no")
        Boolean isSupporter

) {
}
