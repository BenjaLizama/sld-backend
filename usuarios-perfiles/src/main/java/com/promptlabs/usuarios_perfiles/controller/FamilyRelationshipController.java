package com.promptlabs.usuarios_perfiles.controller;

import com.promptlabs.usuarios_perfiles.dto.FamilyMemberDTO;
import com.promptlabs.usuarios_perfiles.dto.LinkFamilyRequest;
import com.promptlabs.usuarios_perfiles.dto.LinkFamilyResponse;
import com.promptlabs.usuarios_perfiles.service.FamilyRelationshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/families")
@RequiredArgsConstructor
@Tag(name = "Parentescos", description = "operaciones relacionadas con la creacion de parentescos")
public class FamilyRelationshipController {

    private final FamilyRelationshipService familyRelationshipService;

    @Operation(summary = "Crear un parentesco entre estudiante y apoderado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parentesco creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PostMapping("/link")
    public ResponseEntity<LinkFamilyResponse> linkFamily(@Valid @RequestBody LinkFamilyRequest request) {
        LinkFamilyResponse response = familyRelationshipService.linkFamily(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "eliminar un parentesco")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parentesco creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/parentesco")
    public ResponseEntity<Void> eliminarParentesco(@Valid @RequestBody LinkFamilyRequest request) {
        familyRelationshipService.unlinkFamily(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "buscar los actores de un parentesco")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actores encontrados"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{rut}/parentesco")
    public ResponseEntity<java.util.List<FamilyMemberDTO>> obtenerApoderados(@PathVariable String rut) {
        java.util.List<FamilyMemberDTO> response = familyRelationshipService.findParents(rut);
        return ResponseEntity.ok(response);
    }
}