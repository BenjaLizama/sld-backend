package com.promptlabs.backend_for_frontend.dto;

public record TeacherProfileDTO(
        String biography,
        String office,
        String availabilityHours,
        String academicGrade
) {
}
