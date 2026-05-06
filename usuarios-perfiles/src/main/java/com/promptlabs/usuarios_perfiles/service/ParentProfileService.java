package com.promptlabs.usuarios_perfiles.service;

import com.promptlabs.usuarios_perfiles.dto.ParentInformationUpdateRequest;
import com.promptlabs.usuarios_perfiles.entity.ParentProfile;
import com.promptlabs.usuarios_perfiles.repository.ParentProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParentProfileService {

    private final ParentProfileRepository parentProfileRepository;

    @Transactional
    public void updateParentInfo(UUID userId, ParentInformationUpdateRequest request) {

        // Buscamos el perfil usando el ID del usuario
        ParentProfile profile = parentProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Perfil de apoderado no encontrado para el usuario: " + userId));

        // Actualizamos los datos
        profile.setEducationLevel(request.educationLevel());
        profile.setIsSupporter(request.isSupporter());

        // Guardamos
        parentProfileRepository.save(profile);

        System.out.println("👨‍👩‍👧 Información de apoderado actualizada para: " + userId);
    }


}
