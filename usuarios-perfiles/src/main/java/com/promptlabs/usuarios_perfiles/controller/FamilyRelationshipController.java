package com.promptlabs.usuarios_perfiles.controller;

import com.promptlabs.usuarios_perfiles.dto.FamilyMemberDTO;
import com.promptlabs.usuarios_perfiles.dto.LinkFamilyRequest;
import com.promptlabs.usuarios_perfiles.dto.LinkFamilyResponse;
import com.promptlabs.usuarios_perfiles.service.FamilyRelationshipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/families")
@RequiredArgsConstructor
public class FamilyRelationshipController {

    private final FamilyRelationshipService familyRelationshipService;
    @PostMapping("/link")
    public ResponseEntity<LinkFamilyResponse> linkFamily(@Valid @RequestBody LinkFamilyRequest request) {
        LinkFamilyResponse response = familyRelationshipService.linkFamily(request);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @DeleteMapping("/parentesco")
    public ResponseEntity<Void> eliminarParentesco(@Valid @RequestBody LinkFamilyRequest request) {
        familyRelationshipService.unlinkFamily(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{rut}/parentesco")
    public ResponseEntity<java.util.List<FamilyMemberDTO>> obtenerApoderados(@PathVariable String rut) {
        java.util.List<FamilyMemberDTO> response = familyRelationshipService.findParents(rut);
        return ResponseEntity.ok(response);
    }
}