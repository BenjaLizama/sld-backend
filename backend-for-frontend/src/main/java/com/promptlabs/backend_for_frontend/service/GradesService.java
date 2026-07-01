package com.promptlabs.backend_for_frontend.service;

import com.promptlabs.backend_for_frontend.client.GradesClient;
import com.promptlabs.backend_for_frontend.dto.grades.GradeRequestDto;
import com.promptlabs.backend_for_frontend.dto.grades.GradeResponseDto;
import com.promptlabs.backend_for_frontend.dto.grades.GradeUpdateDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GradesService {

    private final GradesClient gradesClient;

    public GradeResponseDto addGrade(GradeRequestDto request) {
        return gradesClient.addGrade(request);
    }

    public List<GradeResponseDto> getAllGrades() {
        return gradesClient.findAll();
    }

    public List<GradeResponseDto> getGradesByStudentId(UUID studentId) {
        return gradesClient.findById(studentId);
    }

    public GradeResponseDto updateGrade(UUID id, GradeUpdateDto request) {
        return gradesClient.updateGrade(id, request);
    }

    public void deleteGrade(UUID id) {
        gradesClient.deleteGrade(id);
    }
}