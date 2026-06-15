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
    private final com.promptlabs.usuarios_perfiles.repository.UserRepository userRepository;

    @Transactional
    public void updateParentInfo(UUID userId, ParentInformationUpdateRequest request) {
        ParentProfile profile = parentProfileRepository.findById(userId)
                .orElseGet(() -> {
                    com.promptlabs.usuarios_perfiles.entity.User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado para crear perfil de apoderado: " + userId));
                    ParentProfile newProfile = new ParentProfile();
                    newProfile.setUser(user);
                    return newProfile;
                });

        profile.setEducationLevel(request.educationLevel());
        profile.setIsSupporter(request.isSupporter());

        parentProfileRepository.save(profile);

    }
}
