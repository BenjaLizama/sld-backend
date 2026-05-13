package com.promptlabs.usuarios_perfiles.dto;

import java.util.UUID;

public record AuthRegistrationRequest(
        String email,
        UUID id,
        String role

) {
}
