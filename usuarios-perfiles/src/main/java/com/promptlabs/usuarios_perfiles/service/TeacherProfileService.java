package com.promptlabs.usuarios_perfiles.service;

import com.promptlabs.usuarios_perfiles.dto.TeacherUpdateRequest;
import com.promptlabs.usuarios_perfiles.entity.TeacherProfile;
import com.promptlabs.usuarios_perfiles.repository.TeacherProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherProfileService {

    private final TeacherProfileRepository teacherProfileRepository;

    @Transactional
    public void updateTeacherInfo(UUID userId, TeacherUpdateRequest request) {

        TeacherProfile profile = teacherProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Perfil de profesor no encontrado para el usuario: " + userId));

        profile.setBiography(request.biography());
        profile.setOffice(request.office());
        profile.setAvailabilityHours(request.availabilityHours());
        profile.setAcademicGrade(request.academicGrade());

        teacherProfileRepository.save(profile);

        System.out.println("👨‍🏫 Información de profesor actualizada para: " + userId);
    }
}