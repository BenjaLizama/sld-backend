package com.promptlabs.usuarios_perfiles.service;

import com.promptlabs.usuarios_perfiles.dto.StudentInformationUpdateRequest;
import com.promptlabs.usuarios_perfiles.entity.StudentProfile;
import com.promptlabs.usuarios_perfiles.repository.StudentProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;

    @Transactional
    public void updateMedicalInfo(UUID userId, StudentInformationUpdateRequest request) {

        StudentProfile profile = studentProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Perfil de estudiante no encontrado para el usuario: " + userId));

        profile.setMedicConditions(request.medicConditions());

        studentProfileRepository.save(profile);

        System.out.println("🩺 Información médica actualizada para el estudiante: " + userId);
    }
}