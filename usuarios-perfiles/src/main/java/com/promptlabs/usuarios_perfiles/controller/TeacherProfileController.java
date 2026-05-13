package com.promptlabs.usuarios_perfiles.controller;

import com.promptlabs.usuarios_perfiles.dto.TeacherUpdateRequest;
import com.promptlabs.usuarios_perfiles.service.TeacherProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherProfileController {

    private final TeacherProfileService teacherProfileService;

    @PutMapping("/{userId}/teacher-info")
    public ResponseEntity<String> updateTeacherInfo(
            @PathVariable UUID userId,
            @RequestBody TeacherUpdateRequest request
    ) {
        teacherProfileService.updateTeacherInfo(userId, request);
        return ResponseEntity.ok("Información de profesor actualizada exitosamente");
    }
}