package com.promptlabs.usuarios_perfiles.dto;

import java.time.Instant;
import java.time.LocalDate;

public record UserSummaryDTO(
        String fullName,
        String email,
        String adress,
        Instant creationDate,
        String nationality,
        String gender,
        String rut,
        LocalDate birthday,
        String phoneNumber

) {
}
