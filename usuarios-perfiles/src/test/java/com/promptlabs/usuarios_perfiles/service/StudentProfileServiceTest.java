package com.promptlabs.usuarios_perfiles.service;

import com.promptlabs.usuarios_perfiles.dto.StudentInformationUpdateRequest;
import com.promptlabs.usuarios_perfiles.entity.StudentProfile;
import com.promptlabs.usuarios_perfiles.repository.StudentProfileRepository;
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
class StudentProfileServiceTest {

    @Mock
    private StudentProfileRepository studentProfileRepository;

    @InjectMocks
    private StudentProfileService studentProfileService;

    private UUID userId;
    private StudentInformationUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        updateRequest = new StudentInformationUpdateRequest("Allergies: None");
    }

    @Test
    void updateMedicalInfo_WhenProfileExists_ShouldUpdateProfile() {
        StudentProfile existingProfile = new StudentProfile();
        existingProfile.setId(userId);
        existingProfile.setMedicConditions("Some condition");

        when(studentProfileRepository.findById(userId)).thenReturn(Optional.of(existingProfile));

        studentProfileService.updateMedicalInfo(userId, updateRequest);

        verify(studentProfileRepository).save(existingProfile);
        assertEquals("Allergies: None", existingProfile.getMedicConditions());
    }

    @Test
    void updateMedicalInfo_WhenProfileDoesNotExist_ShouldThrowException() {
        when(studentProfileRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            studentProfileService.updateMedicalInfo(userId, updateRequest)
        );

        assertEquals("Perfil de estudiante no encontrado para el usuario: " + userId, exception.getMessage());
        verify(studentProfileRepository, never()).save(any());
    }
}
