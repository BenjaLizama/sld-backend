package com.promptlabs.usuarios_perfiles.service;

import com.promptlabs.usuarios_perfiles.dto.ParentInformationUpdateRequest;
import com.promptlabs.usuarios_perfiles.entity.ParentProfile;
import com.promptlabs.usuarios_perfiles.repository.ParentProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParentProfileServiceTest {

    @Mock
    private ParentProfileRepository parentProfileRepository;

    @Mock
    private com.promptlabs.usuarios_perfiles.repository.UserRepository userRepository;

    @InjectMocks
    private ParentProfileService parentProfileService;

    private UUID userId;
    private ParentInformationUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        updateRequest = new ParentInformationUpdateRequest("University", true);
    }

    @Test
    void updateParentInfo_WhenProfileExists_ShouldUpdateExistingProfile() {
        ParentProfile existingProfile = new ParentProfile();
        existingProfile.setId(userId);
        existingProfile.setEducationLevel("High School");
        existingProfile.setIsSupporter(false);

        when(parentProfileRepository.findById(userId)).thenReturn(Optional.of(existingProfile));

        parentProfileService.updateParentInfo(userId, updateRequest);

        verify(parentProfileRepository).save(existingProfile);
        assertEquals("University", existingProfile.getEducationLevel());
        assertEquals(true, existingProfile.getIsSupporter());
        verifyNoInteractions(userRepository);
    }

    @Test
    void updateParentInfo_WhenProfileDoesNotExist_ShouldCreateNewProfile() {
        com.promptlabs.usuarios_perfiles.entity.User user = new com.promptlabs.usuarios_perfiles.entity.User();
        user.setId(userId);

        when(parentProfileRepository.findById(userId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        parentProfileService.updateParentInfo(userId, updateRequest);

        ArgumentCaptor<ParentProfile> profileCaptor = ArgumentCaptor.forClass(ParentProfile.class);
        verify(parentProfileRepository).save(profileCaptor.capture());
        
        ParentProfile savedProfile = profileCaptor.getValue();
        assertEquals(user, savedProfile.getUser());
        assertEquals("University", savedProfile.getEducationLevel());
        assertEquals(true, savedProfile.getIsSupporter());
    }

    @Test
    void updateParentInfo_WhenProfileAndUserDoNotExist_ShouldThrowException() {
        when(parentProfileRepository.findById(userId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            parentProfileService.updateParentInfo(userId, updateRequest)
        );

        assertEquals("Usuario no encontrado para crear perfil de apoderado: " + userId, exception.getMessage());
        verify(parentProfileRepository, never()).save(any());
    }
}
