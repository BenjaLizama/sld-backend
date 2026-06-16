package com.promptlabs.backend_for_frontend.dto;

public record LoginRequestDTO(
        LoginData login,
        SessionDTO session

) {
}
