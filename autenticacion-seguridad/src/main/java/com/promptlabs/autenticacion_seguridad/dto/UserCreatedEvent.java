package com.promptlabs.autenticacion_seguridad.dto;

import java.util.UUID;

public record UserCreatedEvent(
        UUID userId,
        String email,
        String role
) {}