package com.promptLabs.ms_notas.controller;


import com.promptLabs.ms_notas.dto.GradeRequestDto;
import com.promptLabs.ms_notas.dto.GradeResponseDto;
import com.promptLabs.ms_notas.dto.GradeUpdateDto;
import com.promptLabs.ms_notas.entity.Grade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.promptLabs.ms_notas.service.GradeService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/grades")
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;

    @PostMapping
    public ResponseEntity<GradeResponseDto> addGrade(@RequestBody @Valid GradeRequestDto gradeRequestDto) {

        GradeResponseDto response = gradeService.addGrade(gradeRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<List<GradeResponseDto>> findAll() {
        List<GradeResponseDto> response = gradeService.listGrades();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<GradeResponseDto> updateGrade(@PathVariable UUID id, @RequestBody @Valid GradeUpdateDto gradeUpdateDto) {
        GradeResponseDto response = gradeService.updateGrade(gradeUpdateDto, id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/student/{id}")
    public ResponseEntity<List<GradeResponseDto>> findById(@PathVariable UUID id) {
        List<GradeResponseDto> response = gradeService.getGrades(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable UUID id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }
}
