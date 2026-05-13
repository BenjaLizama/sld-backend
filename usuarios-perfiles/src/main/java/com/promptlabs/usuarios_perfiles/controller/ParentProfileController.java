package com.promptlabs.usuarios_perfiles.controller;

import com.promptlabs.usuarios_perfiles.dto.ParentInformationUpdateRequest;
import com.promptlabs.usuarios_perfiles.service.ParentProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/parents") // 👨‍👩‍👧 Endpoint exclusivo para apoderados
@RequiredArgsConstructor
public class ParentProfileController {

    private final ParentProfileService parentProfileService;

    @PutMapping("/{userId}/parent-info")
    public ResponseEntity<String> updateParentInfo(
            @PathVariable UUID userId,
            @Valid @RequestBody ParentInformationUpdateRequest request
    ) {
        parentProfileService.updateParentInfo(userId, request);
        return ResponseEntity.ok("Información de apoderado actualizada exitosamente");
    }
}