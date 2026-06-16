package com.promptlabs.usuarios_perfiles.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserProfileCompletionRequest(
        @Schema(description = "RUT del usuario con guion y dígito verificador", example = "12345678-9")
        @NotBlank(message = "El RUT es obligatorio")
        String rut,
        @Schema(description = "Primer nombre", example = "Juan")
        @NotBlank(message = "El primer nombre es obligatorio")
        String firstName,
        @Schema(description = "Segundo nombre", example = "alejandro")
        String middleName, // Opcional, sin anotación
        @Schema(description = "Apellido", example = "perez")
        @NotBlank(message = "El apellido es obligatorio")
        String lastName,
        @Schema(description = "Segundo Apellido", example = "diaz")
        String secondLastName, // Opcional
        @Schema(description = "Numero de telefono", example = "+56936489779")
        @NotBlank(message = "El teléfono es obligatorio")
        String phoneNumber,
        @Schema(description = "direccion", example = "calle x, melipilla")
        @NotBlank(message = "La dirección es obligatoria")
        String address,
        @Schema(description = "Fecha de nacimiento en formato ISO", example = "1995-05-15")
        @NotNull(message = "La fecha de nacimiento es obligatoria")
        LocalDate birthday,
        @Schema(description = "Nacionalidad", example = "Hamaiquino")
        @NotBlank(message = "La nacionalidad es obligatoria")
        String nationality,
        @Schema(description = "Genero", example = "1= Femenino ,2 = Masculino,3 = Otro")
        @NotNull(message = "El género es obligatorio")
        Long genderId
) {
}