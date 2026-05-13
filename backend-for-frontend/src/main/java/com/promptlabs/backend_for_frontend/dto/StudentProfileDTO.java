package com.promptlabs.backend_for_frontend.dto;

import jakarta.validation.constraints.Size;

public record StudentProfileDTO(

        @Size(max = 255, message = "La descripción es demasiado larga")
        String medicConditions
) {
}
