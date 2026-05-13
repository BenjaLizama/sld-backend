package com.promptlabs.backend_for_frontend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserInfoRequestDTO(

        @NotBlank(message = "El RUT es obligatorio")
        String rut,
        @NotBlank(message = "El primer nombre es obligatorio")
        String firstName,
        String middleName,
        @NotBlank(message = "El apellido es obligatorio")
        String lastName,
        String secondLastName,
        @NotBlank(message = "El teléfono es obligatorio")
        String phoneNumber,
        @NotBlank(message = "La dirección es obligatoria")
        String address,
        @NotNull(message = "La fecha de nacimiento es obligatoria")
        LocalDate birthday,
        @NotBlank(message = "La nacionalidad es obligatoria")
        String nationality,
        @NotNull(message = "El género es obligatorio")
        Long genderId

) {
}
