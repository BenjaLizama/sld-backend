package com.promptlabs.backend_for_frontend.dto;

import java.time.Instant;
import java.time.LocalDate;

public record UserSummaryResponse(
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