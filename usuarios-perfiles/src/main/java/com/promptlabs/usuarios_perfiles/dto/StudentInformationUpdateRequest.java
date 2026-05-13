package com.promptlabs.usuarios_perfiles.dto;


import jakarta.validation.constraints.Size;

public record StudentInformationUpdateRequest(

        @Size(max = 255, message = "La descripción es demasiado larga")
        String medicConditions
) {}
