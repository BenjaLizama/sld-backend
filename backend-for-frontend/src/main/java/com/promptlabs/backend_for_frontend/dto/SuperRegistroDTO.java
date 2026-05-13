package com.promptlabs.backend_for_frontend.dto;

public record SuperRegistroDTO(
        AuthRequestDTO auth,
        SessionDTO session,
        @com.fasterxml.jackson.annotation.JsonProperty("profile")
        Object profile,
        UserInfoRequestDTO personal
) {
}
