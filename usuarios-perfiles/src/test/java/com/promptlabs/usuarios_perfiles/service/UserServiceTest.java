package com.promptlabs.usuarios_perfiles.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptlabs.usuarios_perfiles.dto.*;
import com.promptlabs.usuarios_perfiles.entity.Gender;
import com.promptlabs.usuarios_perfiles.entity.User;
import com.promptlabs.usuarios_perfiles.repository.GenderRepository;
import com.promptlabs.usuarios_perfiles.repository.UserRepository;
import com.promptlabs.usuarios_perfiles.service.strategy.ProfileCreationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private GenderRepository genderRepository;
    @Mock
    private TeacherProfileService teacherProfileService;
    @Mock
    private StudentProfileService studentProfileService;
    @Mock
    private ParentProfileService parentProfileService;
    @Mock
    private ObjectMapper objectMapper;

    // Mocks para las estrategias
    @Mock
    private ProfileCreationStrategy teacherStrategy;
    @Mock
    private ProfileCreationStrategy studentStrategy;

    private UserService userService;

    @BeforeEach
    void setUp() {
        // Configuramos los mocks de las estrategias para que devuelvan un rol
        lenient().when(teacherStrategy.getRole()).thenReturn("ROLE_TEACHER");
        lenient().when(studentStrategy.getRole()).thenReturn("ROLE_STUDENT");

        List<ProfileCreationStrategy> strategies = Arrays.asList(teacherStrategy, studentStrategy);
        
        userService = new UserService(
                userRepository,
                strategies,
                genderRepository,
                teacherProfileService,
                studentProfileService,
                parentProfileService,
                objectMapper
        );
    }

    @Test
    void crearUserCascaron_WithValidStrategy_ShouldSaveUser() {
        AuthRegistrationRequest request = new AuthRegistrationRequest("test@test.com", UUID.randomUUID(), "ROLE_TEACHER");

        userService.crearUserCascaron(request);

        verify(teacherStrategy).createEmptyProfile(any(User.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void crearUserCascaron_WithRoleUser_ShouldNotUseStrategy() {
        AuthRegistrationRequest request = new AuthRegistrationRequest("test@test.com", UUID.randomUUID(), "ROLE_USER");

        userService.crearUserCascaron(request);

        verify(teacherStrategy, never()).createEmptyProfile(any());
        verify(studentStrategy, never()).createEmptyProfile(any());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void crearUserCascaron_WithMissingStrategy_ShouldThrowException() {
        AuthRegistrationRequest request = new AuthRegistrationRequest("test@test.com", UUID.randomUUID(), "ROLE_ADMIN");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.crearUserCascaron(request));
        assertTrue(exception.getMessage().contains("no tiene estrategia"));
    }

    @Test
    void completarPerfilBase_WhenSuccessful_ShouldUpdateUser() {
        UUID userId = UUID.randomUUID();
        // UserProfileCompletionRequest(rut, firstName, middleName, lastName, secondLastName, phoneNumber, address, birthday, nationality, genderId)
        UserProfileCompletionRequest request = new UserProfileCompletionRequest(
                "12345678-9", "John", "Doe", "Smith", "Johnson",
                "123456789", "Street 123", LocalDate.now(), "CL", 1L
        );

        Gender gender = new Gender();
        User user = new User();

        when(genderRepository.findById(1L)).thenReturn(Optional.of(gender));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.completarPerfilBase(userId, request);

        verify(userRepository).save(user);
        assertEquals("12345678-9", user.getRut());
        assertEquals("John", user.getFirstName());
    }

    @Test
    void completarPerfilBase_WhenGenderNotFound_ShouldThrowException() {
        UUID userId = UUID.randomUUID();
        UserProfileCompletionRequest request = new UserProfileCompletionRequest(
                "12345678-9", "John", "Doe", "Smith", "Johnson",
                "123456789", "Street 123", LocalDate.now(), "CL", 1L
        );

        when(genderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.completarPerfilBase(userId, request));
    }

    @Test
    void actualizarPerfilEspecifico_ForTeacher_ShouldCallTeacherService() {
        UUID userId = UUID.randomUUID();
        // Payload: {"roles": ["ROLE_TEACHER"]} -> eyJyb2xlcyI6IFsiUk9MRV9URUFDSEVSIl19
        String token = "Bearer abc.eyJyb2xlcyI6IFsiUk9MRV9URUFDSEVSIl19.xyz";
        Map<String, Object> data = new HashMap<>();
        TeacherUpdateRequest dto = new TeacherUpdateRequest("Bio", "Office", "9-17", "Master");

        when(objectMapper.convertValue(data, TeacherUpdateRequest.class)).thenReturn(dto);

        userService.actualizarPerfilEspecifico(userId, token, data);

        verify(teacherProfileService).updateTeacherInfo(userId, dto);
    }

    @Test
    void actualizarPerfilEspecifico_ForStudent_ShouldCallStudentService() {
        UUID userId = UUID.randomUUID();
        // Payload: {"roles": ["ROLE_STUDENT"]} -> eyJyb2xlcyI6IFsiUk9MRV9TVFVERU5UIl19
        String token = "Bearer abc.eyJyb2xlcyI6IFsiUk9MRV9TVFVERU5UIl19.xyz";
        Map<String, Object> data = new HashMap<>();
        StudentInformationUpdateRequest dto = new StudentInformationUpdateRequest("None");

        when(objectMapper.convertValue(data, StudentInformationUpdateRequest.class)).thenReturn(dto);

        userService.actualizarPerfilEspecifico(userId, token, data);

        verify(studentProfileService).updateMedicalInfo(userId, dto);
    }

    @Test
    void actualizarPerfilEspecifico_ForParent_ShouldCallParentService() {
        UUID userId = UUID.randomUUID();
        // Payload: {"roles":["ROLE_PARENT"]} -> eyJyb2xlcyI6WyJST0xFX1BBUkVOVCJdfQ==
        String token = "Bearer abc.eyJyb2xlcyI6WyJST0xFX1BBUkVOVCJdfQ==.xyz";
        Map<String, Object> data = new HashMap<>();
        ParentInformationUpdateRequest dto = new ParentInformationUpdateRequest("High", true);

        when(objectMapper.convertValue(data, ParentInformationUpdateRequest.class)).thenReturn(dto);

        userService.actualizarPerfilEspecifico(userId, token, data);

        verify(parentProfileService).updateParentInfo(userId, dto);
    }

    @Test
    void actualizarPerfilEspecifico_ForRoleUser_ShouldThrowException() {
        UUID userId = UUID.randomUUID();
        // Payload: {"roles":["ROLE_USER"]} -> eyJyb2xlcyI6WyJST0xFX1VTRVIiXX0=
        String token = "Bearer abc.eyJyb2xlcyI6WyJST0xFX1VTRVIiXX0=.xyz";
        Map<String, Object> data = new HashMap<>();

        assertThrows(RuntimeException.class, () -> userService.actualizarPerfilEspecifico(userId, token, data));
    }
}

