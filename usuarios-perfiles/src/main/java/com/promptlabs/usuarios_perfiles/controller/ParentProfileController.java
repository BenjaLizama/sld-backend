package com.promptlabs.usuarios_perfiles.controller;

import com.promptlabs.usuarios_perfiles.dto.ParentInformationUpdateRequest;
import com.promptlabs.usuarios_perfiles.service.ParentProfileService;
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
@RequestMapping("/api/v1/parents")
@RequiredArgsConstructor
//@Tag(name = "Apoderados", description = "operaciones relacionadas con el perfil de apoderado")
public class ParentProfileController {

    private final ParentProfileService parentProfileService;

    @Operation(summary = "actualizar el perfil de un apoderado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado")
    })
    @PutMapping("/{userId}/parent-info")
    public ResponseEntity<String> updateParentInfo(
            @Parameter(description = "ID único del usuario (UUID)") @PathVariable UUID userId,
            @Valid @RequestBody ParentInformationUpdateRequest request
    ) {
        parentProfileService.updateParentInfo(userId, request);
        return ResponseEntity.ok("Información de apoderado actualizada exitosamente");
    }
}