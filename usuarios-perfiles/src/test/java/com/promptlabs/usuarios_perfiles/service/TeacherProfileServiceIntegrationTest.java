package com.promptlabs.usuarios_perfiles.service;

import com.promptlabs.usuarios_perfiles.dto.TeacherUpdateRequest;
import com.promptlabs.usuarios_perfiles.entity.TeacherProfile;
import com.promptlabs.usuarios_perfiles.entity.User;
import com.promptlabs.usuarios_perfiles.repository.TeacherProfileRepository;
import com.promptlabs.usuarios_perfiles.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TeacherProfileServiceIntegrationTest {

    @Autowired
    private TeacherProfileService teacherProfileService;

    @Autowired
    private TeacherProfileRepository teacherProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void updateTeacherInfo_ShouldUpdateExistingProfile() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).email("teacher_int@test.com").build();
        userRepository.save(user);
        
        TeacherProfile profile = TeacherProfile.builder().user(user).build();
        teacherProfileRepository.save(profile);

        TeacherUpdateRequest request = new TeacherUpdateRequest(
                "Senior Professor", "Room 404", "All day", "Master"
        );

        teacherProfileService.updateTeacherInfo(userId, request);

        TeacherProfile updatedProfile = teacherProfileRepository.findById(userId).orElseThrow();
        assertEquals("Senior Professor", updatedProfile.getBiography());
        assertEquals("Room 404", updatedProfile.getOffice());
        assertEquals("All day", updatedProfile.getAvailabilityHours());
        assertEquals("Master", updatedProfile.getAcademicGrade());
    }

    @Test
    void updateTeacherInfo_WhenProfileNotFound_ShouldThrowException() {
        UUID userId = UUID.randomUUID();
        TeacherUpdateRequest request = new TeacherUpdateRequest("Bio", "Office", "Hours", "Grade");

        assertThrows(RuntimeException.class, () -> 
            teacherProfileService.updateTeacherInfo(userId, request)
        );
    }
}
