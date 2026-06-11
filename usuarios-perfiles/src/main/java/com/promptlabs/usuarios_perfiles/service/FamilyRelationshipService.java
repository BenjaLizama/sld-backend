package com.promptlabs.usuarios_perfiles.service;

import com.promptlabs.usuarios_perfiles.dto.FamilyMemberDTO;
import com.promptlabs.usuarios_perfiles.dto.LinkFamilyRequest;
import com.promptlabs.usuarios_perfiles.dto.LinkFamilyResponse;
import com.promptlabs.usuarios_perfiles.entity.FamilyRelationship;
import com.promptlabs.usuarios_perfiles.entity.ParentProfile;
import com.promptlabs.usuarios_perfiles.entity.StudentProfile;
import com.promptlabs.usuarios_perfiles.entity.ParentType;
import com.promptlabs.usuarios_perfiles.exception.RelationshipException;
import com.promptlabs.usuarios_perfiles.repository.FamilyRelationshipRepository;
import com.promptlabs.usuarios_perfiles.repository.ParentProfileRepository;
import com.promptlabs.usuarios_perfiles.repository.ParentTypeRepository;
import com.promptlabs.usuarios_perfiles.repository.StudentProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FamilyRelationshipService {

    private final FamilyRelationshipRepository familyRelationshipRepository;
    private final ParentProfileRepository parentProfileRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final ParentTypeRepository parentTypeRepository;

    @Transactional
    public LinkFamilyResponse linkFamily(LinkFamilyRequest request) {


        ParentProfile parent = parentProfileRepository.findByUserRut(request.parentId())
                .orElseThrow(() -> new EntityNotFoundException("No se a encontrado al apoderado: " + request.parentId()));

        StudentProfile student = studentProfileRepository.findByUserRut(request.studentId())
                .orElseThrow(() -> new EntityNotFoundException("No se a encontrado al estudiante: " + request.studentId()));

        ParentType parentType = parentTypeRepository.findById(request.parentTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de parentesco no encontrado: " + request.parentTypeId()));


        FamilyRelationship relationship = new FamilyRelationship();
        relationship.setParentProfile(parent);
        relationship.setStudentProfile(student);
        relationship.setParentType(parentType);
        boolean existe = familyRelationshipRepository.existsByParentProfileAndStudentProfile(parent, student);
        if (existe) {
            throw new RelationshipException("El parentesco entre este apoderado y estudiante ya existe.");
        }


        familyRelationshipRepository.save(relationship);
        return new LinkFamilyResponse("se creo correctamente la relacion ", request.parentId(), request.studentId());
    }

    @Transactional
    public void unlinkFamily(LinkFamilyRequest request) {

        ParentProfile parent = parentProfileRepository.findByUserRut(request.parentId())
                .orElseThrow(() -> new EntityNotFoundException("No se a encontrado al apoderado: " + request.parentId()));

        StudentProfile student = studentProfileRepository.findByUserRut(request.studentId())
                .orElseThrow(() -> new EntityNotFoundException("No se a encontrado al estudiante: " + request.studentId()));

        FamilyRelationship relationship = familyRelationshipRepository.
                findByParentProfileAndStudentProfile(parent, student).orElseThrow(() -> new RelationshipException("no exciste relacion entre: " + request.parentId() + request.studentId()));
        familyRelationshipRepository.delete(relationship);
    }

    @Transactional(readOnly = true)
    public List<FamilyMemberDTO> findParents(String studentRut) {
        if (!studentProfileRepository.existsByUserRut(studentRut)) {
            throw new EntityNotFoundException("No se ha encontrado al estudiante con RUT: " + studentRut);
        }
        return familyRelationshipRepository.findByStudentProfileUserRut(studentRut).stream()
                .map(rel -> new FamilyMemberDTO(
                        rel.getParentProfile().getUser().getFirstName(), rel.getParentProfile().getUser().getRut(), rel.getParentType().getRelationship())).collect(java.util.stream.Collectors.toList());
    }

}