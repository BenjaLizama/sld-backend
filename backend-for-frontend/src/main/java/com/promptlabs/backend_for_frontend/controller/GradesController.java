package com.promptlabs.backend_for_frontend.controller;

import com.promptlabs.backend_for_frontend.dto.grades.GradeRequestDto;
import com.promptlabs.backend_for_frontend.dto.grades.GradeResponseDto;
import com.promptlabs.backend_for_frontend.dto.grades.GradeUpdateDto;
import com.promptlabs.backend_for_frontend.dto.grades.StudentWithGradesDto;
import com.promptlabs.backend_for_frontend.service.GradesService;
import com.promptlabs.backend_for_frontend.service.StudentGradesOrchestratorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bff/grades")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GradesController {

    private final GradesService gradesService;
    private final StudentGradesOrchestratorService orchestratorService;

    @PostMapping
    public ResponseEntity<GradeResponseDto> createGrade(@RequestBody @Valid GradeRequestDto request) {
        GradeResponseDto response = gradesService.addGrade(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<StudentWithGradesDto>> getDashboardData() {
        List<StudentWithGradesDto> data = orchestratorService.getStudentsWithTheirGrades();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<GradeResponseDto>> getGradesByStudent(@PathVariable UUID studentId) {
        List<GradeResponseDto> response = gradesService.getGradesByStudentId(studentId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GradeResponseDto> updateGrade(
            @PathVariable UUID id,
            @RequestBody @Valid GradeUpdateDto request
    ) {
        GradeResponseDto response = gradesService.updateGrade(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable UUID id) {
        gradesService.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }
}