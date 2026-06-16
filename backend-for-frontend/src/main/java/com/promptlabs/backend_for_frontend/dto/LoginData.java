package com.promptlabs.backend_for_frontend.dto;

public record LoginData(
        String identifier,
        String password,
        String provider
) {
}
