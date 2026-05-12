package com.promptlabs.backend_for_frontend.dto;

public record SuperRegistroDTO(
        AuthRequestDTO auth,
        SessionDTO session,
        ParentProfileDTO profile,
        UserInfoRequestDTO personal
) {
}
