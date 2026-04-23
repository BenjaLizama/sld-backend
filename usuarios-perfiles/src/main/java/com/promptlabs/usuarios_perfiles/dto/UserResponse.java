package com.promptlabs.usuarios_perfiles.dto;

public record UserResponse(
        String email,
        String Name,
        String role,
        String message
) {
}
