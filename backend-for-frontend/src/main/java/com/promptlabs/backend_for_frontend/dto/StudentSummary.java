package com.promptlabs.backend_for_frontend.dto;

import java.util.UUID;

public record StudentSummary(
        UUID studentId,
        String rut,
        String fullName
) {}