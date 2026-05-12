package com.promptlabs.usuarios_perfiles.dto;

import java.util.UUID;

public record UserCreatedEvent(
        UUID userId,
        String email,
        String role
) {}