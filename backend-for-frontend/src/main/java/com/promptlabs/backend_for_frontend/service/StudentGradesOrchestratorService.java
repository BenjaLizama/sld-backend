package com.promptlabs.backend_for_frontend.service;

import com.promptlabs.backend_for_frontend.client.GradesClient;
import com.promptlabs.backend_for_frontend.client.UserClient;
import com.promptlabs.backend_for_frontend.dto.grades.GradeResponseDto;
import com.promptlabs.backend_for_frontend.dto.StudentSummary;
import com.promptlabs.backend_for_frontend.dto.grades.StudentWithGradesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentGradesOrchestratorService {

    private final UserClient userClient; // Se conecta a usuarios-perfiles
    private final GradesClient gradesClient;   // Se conecta a ms-notas

    public List<StudentWithGradesDto> getStudentsWithTheirGrades() {
        // 1. Llamamos a usuarios-perfiles para obtener la lista de alumnos en el sistema
        List<StudentSummary> students = userClient.listStudentsSummary();

        List<StudentWithGradesDto> responseList = new ArrayList<>();

        // 2. Por cada estudiante, viajamos al ms-notas a buscar su historial
        for (StudentSummary student : students) {
            List<GradeResponseDto> studentGrades;
            try {
                // Buscamos las notas en el ms-notas usando el UUID del alumno
                studentGrades = gradesClient.findById(student.studentId());
            } catch (Exception e) {
                // Si el ms-notas falla o no encuentra notas, devolvemos una lista vacía para ese alumno
                studentGrades = List.of();
            }

            // 3. Juntamos la información en nuestro DTO unificado
            StudentWithGradesDto unifiedDto = new StudentWithGradesDto(
                    student.studentId(),
                    student.rut(),
                    student.fullName(),
                    studentGrades
            );

            responseList.add(unifiedDto);
        }

        return responseList;
    }
}