package com.promptLabs.ms_notas.service;

import com.promptLabs.ms_notas.dto.GradeRequestDto;
import com.promptLabs.ms_notas.dto.GradeResponseDto;
import com.promptLabs.ms_notas.dto.GradeUpdateDto;
import com.promptLabs.ms_notas.entity.Grade;
import com.promptLabs.ms_notas.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.promptLabs.ms_notas.repository.GradeRepository;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GradeService {
    private final GradeRepository repository;

    @Transactional
    public GradeResponseDto addGrade(GradeRequestDto requestDto) {

        Grade grade = new Grade();
        grade.setName(requestDto.name());
        grade.setValue(requestDto.value());
        grade.setTeacherId(requestDto.teacherId());
        grade.setStudentId(requestDto.studentId());
        Grade savedGrade = repository.save(grade);

        return new GradeResponseDto(savedGrade.getValue(),
                savedGrade.getName(), savedGrade.getId(),savedGrade.getStudentId(),savedGrade.getTeacherId());

    }

    @Transactional(readOnly = true)
    public List<GradeResponseDto> listGrades() {
        List<Grade> grades = repository.findAll();
        return grades.stream().map(this::convertToDto).toList();
    }

    @Transactional
    public GradeResponseDto updateGrade(GradeUpdateDto requestDto, UUID id) {

        Grade savedGrade = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade: " + id + " no existe"));


        if (requestDto.name() != null) {
            savedGrade.setName(requestDto.name());
        }
        if (requestDto.value() != null) {
            savedGrade.setValue(requestDto.value());
        }

        return new GradeResponseDto(
                savedGrade.getValue(),
                savedGrade.getName(),
                id,
                savedGrade.getStudentId(),
                savedGrade.getTeacherId());

    }

    @Transactional
    public void deleteGrade(UUID id) {
        Grade grade= repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Grade: " + id + " no existe"));
        repository.delete(grade);

    }
    @Transactional
    public List<GradeResponseDto> getGrades(UUID id) {
        List<Grade> grades = repository.findByStudentId(id);
        return grades.stream().map(this::convertToDto).toList();
    }

    private GradeResponseDto convertToDto(Grade grade) {
        return new GradeResponseDto(
                grade.getValue(),
                grade.getName(),
                grade.getId(),
                grade.getStudentId(),
                grade.getTeacherId()
        );
    }

}

