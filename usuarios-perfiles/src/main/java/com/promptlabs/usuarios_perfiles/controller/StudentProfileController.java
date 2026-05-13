package com.promptlabs.usuarios_perfiles.controller;

import com.promptlabs.usuarios_perfiles.dto.StudentInformationUpdateRequest;
import com.promptlabs.usuarios_perfiles.service.StudentProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    @PutMapping("/{userId}/medical-info")
    public ResponseEntity<String> updateMedicalInfo(
            @PathVariable UUID userId,
            @Valid @RequestBody StudentInformationUpdateRequest request
    ) {
        studentProfileService.updateMedicalInfo(userId, request);
        return ResponseEntity.ok("Información médica actualizada exitosamente");
    }
}