package com.promptlabs.usuarios_perfiles.service;

import com.promptlabs.usuarios_perfiles.dto.ParentInformationUpdateRequest;
import com.promptlabs.usuarios_perfiles.entity.ParentProfile;
import com.promptlabs.usuarios_perfiles.entity.User;
import com.promptlabs.usuarios_perfiles.repository.ParentProfileRepository;
import com.promptlabs.usuarios_perfiles.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ParentProfileServiceIntegrationTest {

    @Autowired
    private ParentProfileService parentProfileService;

    @Autowired
    private ParentProfileRepository parentProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void updateParentInfo_ShouldCreateAndSaveProfile() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).email("parent_int@test.com").build();
        userRepository.save(user);

        ParentInformationUpdateRequest request = new ParentInformationUpdateRequest("Master", true);

        parentProfileService.updateParentInfo(userId, request);

        ParentProfile savedProfile = parentProfileRepository.findById(userId).orElseThrow();
        assertEquals("Master", savedProfile.getEducationLevel());
        assertTrue(savedProfile.getIsSupporter());
        assertEquals(userId, savedProfile.getId());
    }

    @Test
    void updateParentInfo_ShouldUpdateExistingProfile() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).email("parent_int_update@test.com").build();
        userRepository.save(user);
        
        ParentProfile initialProfile = ParentProfile.builder().user(user).educationLevel("None").isSupporter(false).build();
        parentProfileRepository.save(initialProfile);

        ParentInformationUpdateRequest request = new ParentInformationUpdateRequest("PhD", true);

        parentProfileService.updateParentInfo(userId, request);

        ParentProfile updatedProfile = parentProfileRepository.findById(userId).orElseThrow();
        assertEquals("PhD", updatedProfile.getEducationLevel());
        assertTrue(updatedProfile.getIsSupporter());
    }
}
