package com.promptlabs.usuarios_perfiles.dto;

public record TeacherUpdateRequest(
        String biography,
        String office,
        String availabilityHours,
        String academicGrade
) {}