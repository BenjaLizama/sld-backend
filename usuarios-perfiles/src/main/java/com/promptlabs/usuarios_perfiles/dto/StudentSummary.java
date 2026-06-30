package com.promptlabs.usuarios_perfiles.dto;

import java.util.UUID;

public record StudentSummary(
        UUID studentId,
        String rut,
        String fullName
) {
}
