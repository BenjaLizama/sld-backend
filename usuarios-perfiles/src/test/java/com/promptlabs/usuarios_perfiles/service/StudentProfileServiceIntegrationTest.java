package com.promptlabs.usuarios_perfiles.service;

import com.promptlabs.usuarios_perfiles.dto.StudentInformationUpdateRequest;
import com.promptlabs.usuarios_perfiles.entity.StudentProfile;
import com.promptlabs.usuarios_perfiles.entity.User;
import com.promptlabs.usuarios_perfiles.repository.StudentProfileRepository;
import com.promptlabs.usuarios_perfiles.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StudentProfileServiceIntegrationTest {

    @Autowired
    private StudentProfileService studentProfileService;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void updateMedicalInfo_ShouldUpdateExistingProfile() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).email("student_int@test.com").build();
        userRepository.save(user);
        
        StudentProfile profile = StudentProfile.builder().user(user).medicConditions("Initial").build();
        studentProfileRepository.save(profile);

        StudentInformationUpdateRequest request = new StudentInformationUpdateRequest("Updated conditions");

        studentProfileService.updateMedicalInfo(userId, request);

        StudentProfile updatedProfile = studentProfileRepository.findById(userId).orElseThrow();
        assertEquals("Updated conditions", updatedProfile.getMedicConditions());
    }

    @Test
    void updateMedicalInfo_WhenProfileNotFound_ShouldThrowException() {
        UUID userId = UUID.randomUUID();
        StudentInformationUpdateRequest request = new StudentInformationUpdateRequest("Conditions");

        assertThrows(RuntimeException.class, () -> 
            studentProfileService.updateMedicalInfo(userId, request)
        );
    }
}
