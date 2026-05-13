package com.promptlabs.usuarios_perfiles.service;

import com.promptlabs.usuarios_perfiles.dto.LinkFamilyRequest;
import com.promptlabs.usuarios_perfiles.entity.FamilyRelationship;
import com.promptlabs.usuarios_perfiles.entity.ParentProfile;
import com.promptlabs.usuarios_perfiles.entity.StudentProfile;
import com.promptlabs.usuarios_perfiles.entity.ParentType;
import com.promptlabs.usuarios_perfiles.repository.FamilyRelationshipRepository;
import com.promptlabs.usuarios_perfiles.repository.ParentProfileRepository;
import com.promptlabs.usuarios_perfiles.repository.ParentTypeRepository;
import com.promptlabs.usuarios_perfiles.repository.StudentProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FamilyRelationshipService {

    private final FamilyRelationshipRepository familyRelationshipRepository;
    private final ParentProfileRepository parentProfileRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final ParentTypeRepository parentTypeRepository;

    @Transactional
    public void linkFamily(LinkFamilyRequest request) {

        // 1. Buscamos a los 3 involucrados
        ParentProfile parent = parentProfileRepository.findById(request.parentId())
                .orElseThrow(() -> new RuntimeException("Apoderado no encontrado"));

        StudentProfile student = studentProfileRepository.findById(request.studentId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        ParentType parentType = parentTypeRepository.findById(request.parentTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de parentesco no encontrado"));

        // 2. Armamos tu entidad FamilyRelationship
        FamilyRelationship relationship = new FamilyRelationship();
        relationship.setParentProfile(parent);
        relationship.setStudentProfile(student);
        relationship.setParentType(parentType);

        // 3. ¡Guardamos la unión!
        familyRelationshipRepository.save(relationship);

        System.out.println("🔗 Familia vinculada con éxito.");
    }
}