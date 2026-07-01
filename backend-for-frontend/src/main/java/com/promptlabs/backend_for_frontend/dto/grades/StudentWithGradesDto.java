package com.promptlabs.backend_for_frontend.dto.grades;


import java.util.List;
import java.util.UUID;

public record StudentWithGradesDto(
        UUID studentId,
        String rut,
        String fullName,
        List<GradeResponseDto> grades
) {}