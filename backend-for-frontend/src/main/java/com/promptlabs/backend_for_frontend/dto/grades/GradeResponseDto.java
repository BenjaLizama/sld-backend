package com.promptlabs.backend_for_frontend.dto.grades;

import java.util.UUID;

public record GradeResponseDto(
        Double valor,
        String name,
        UUID id,
        UUID studentId,
        UUID teacherId
) {

}