package com.promptlabs.usuarios_perfiles.controller;

import com.promptlabs.usuarios_perfiles.dto.StudentInformationUpdateRequest;
import com.promptlabs.usuarios_perfiles.service.StudentProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Tag(name = "Estudiantes", description = "operaciones relacionadas con el perfil de estudiante")
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    @Operation(summary = "actualizar el perfil de un estudiante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado")
    })
    @PutMapping("/{userId}/medical-info")
    public ResponseEntity<String> updateMedicalInfo(
            @Parameter(description = "ID único del usuario (UUID)") @PathVariable UUID userId,
            @Valid @RequestBody StudentInformationUpdateRequest request
    ) {
        studentProfileService.updateMedicalInfo(userId, request);
        return ResponseEntity.ok("Información médica actualizada exitosamente");
    }
}