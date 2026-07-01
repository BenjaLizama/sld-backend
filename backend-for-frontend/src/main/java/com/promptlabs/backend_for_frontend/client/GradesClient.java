package com.promptlabs.backend_for_frontend.client;

import com.promptlabs.backend_for_frontend.config.FeignConfig;
import com.promptlabs.backend_for_frontend.dto.grades.GradeRequestDto;
import com.promptlabs.backend_for_frontend.dto.grades.GradeResponseDto;
import com.promptlabs.backend_for_frontend.dto.grades.GradeUpdateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "servicio-de-notas", url = "${services.grades-service.url}", configuration = FeignConfig.class )
public interface GradesClient {
    @PostMapping("/api/v1/grades")
    GradeResponseDto addGrade(@RequestBody GradeRequestDto gradeRequestDto);

    @GetMapping("/api/v1/grades")
    List<GradeResponseDto> findAll();

    @PutMapping("/api/v1/grades/{id}")
    GradeResponseDto updateGrade(@PathVariable("id") UUID id, @RequestBody GradeUpdateDto gradeUpdateDto);

    @GetMapping("/api/v1/grades/student/{id}")
    List<GradeResponseDto> findById(@PathVariable("id") UUID id);

    @DeleteMapping("/api/v1/grades/{id}")
    void deleteGrade(@PathVariable("id") UUID id);
}
