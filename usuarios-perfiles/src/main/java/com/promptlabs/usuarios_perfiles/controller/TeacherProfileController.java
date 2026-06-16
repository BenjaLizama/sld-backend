package com.promptlabs.usuarios_perfiles.controller;

import com.promptlabs.usuarios_perfiles.dto.TeacherUpdateRequest;
import com.promptlabs.usuarios_perfiles.service.TeacherProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
@Tag(name = "Profesores", description = "operaciones relacionadas con el perfil de profesor")
public class TeacherProfileController {

    private final TeacherProfileService teacherProfileService;

    @Operation(summary = "actualizar el perfil de un profesor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado")
    })

    @PutMapping("/{userId}/teacher-info")
    public ResponseEntity<String> updateTeacherInfo(
            @Parameter(description = "ID único del usuario (UUID)") @PathVariable UUID userId,
            @RequestBody TeacherUpdateRequest request
    ) {
        teacherProfileService.updateTeacherInfo(userId, request);
        return ResponseEntity.ok("Información de profesor actualizada exitosamente");
    }
}