package com.promptlabs.usuarios_perfiles.controller;

import com.promptlabs.usuarios_perfiles.dto.LinkFamilyRequest;
import com.promptlabs.usuarios_perfiles.service.FamilyRelationshipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/families")
@RequiredArgsConstructor
public class FamilyRelationshipController {

    private final FamilyRelationshipService familyRelationshipService;

    @PostMapping("/link")
    public ResponseEntity<String> linkFamily(@Valid @RequestBody LinkFamilyRequest request) {
        familyRelationshipService.linkFamily(request);
        return ResponseEntity.ok("Vínculo familiar creado con éxito");
    }
}