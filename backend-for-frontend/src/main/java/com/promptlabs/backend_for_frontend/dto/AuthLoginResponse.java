package com.promptlabs.backend_for_frontend.dto;

import java.time.LocalDateTime;

public record AuthLoginResponse(
        String accessToken,
        String refreshToken,
        LocalDateTime expiresAt
) {
}
