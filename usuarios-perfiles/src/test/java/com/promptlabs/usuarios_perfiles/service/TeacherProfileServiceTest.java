package com.promptlabs.usuarios_perfiles.service;

import com.promptlabs.usuarios_perfiles.dto.TeacherUpdateRequest;
import com.promptlabs.usuarios_perfiles.entity.TeacherProfile;
import com.promptlabs.usuarios_perfiles.repository.TeacherProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherProfileServiceTest {

    @Mock
    private TeacherProfileRepository teacherProfileRepository;

    @InjectMocks
    private TeacherProfileService teacherProfileService;

    private UUID userId;
    private TeacherUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        updateRequest = new TeacherUpdateRequest(
                "Bio", "Office 1", "Mon-Fri 8-10", "PhD"
        );
    }

    @Test
    void updateTeacherInfo_WhenProfileExists_ShouldUpdateProfile() {
        TeacherProfile existingProfile = new TeacherProfile();
        existingProfile.setId(userId);

        when(teacherProfileRepository.findById(userId)).thenReturn(Optional.of(existingProfile));

        teacherProfileService.updateTeacherInfo(userId, updateRequest);

        verify(teacherProfileRepository).save(existingProfile);
        assertEquals("Bio", existingProfile.getBiography());
        assertEquals("Office 1", existingProfile.getOffice());
        assertEquals("Mon-Fri 8-10", existingProfile.getAvailabilityHours());
        assertEquals("PhD", existingProfile.getAcademicGrade());
    }

    @Test
    void updateTeacherInfo_WhenProfileDoesNotExist_ShouldThrowException() {
        when(teacherProfileRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            teacherProfileService.updateTeacherInfo(userId, updateRequest)
        );

        assertEquals("Perfil de profesor no encontrado para el usuario: " + userId, exception.getMessage());
        verify(teacherProfileRepository, never()).save(any());
    }
}
