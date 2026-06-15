package com.promptlabs.usuarios_perfiles.service;

import com.promptlabs.usuarios_perfiles.dto.*;
import com.promptlabs.usuarios_perfiles.entity.*;
import com.promptlabs.usuarios_perfiles.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private TeacherProfileRepository teacherProfileRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private ParentProfileRepository parentProfileRepository;

    private Gender masculino;

    @BeforeEach
    void setUp() {
        masculino = genderRepository.save(new Gender(1L, "MASCULINO"));
    }

    @Test
    void crearUserCascaron_Teacher_ShouldCreateUserAndTeacherProfile() {
        UUID userId = UUID.randomUUID();
        AuthRegistrationRequest request = new AuthRegistrationRequest("teacher@test.com", userId, "ROLE_TEACHER");

        userService.crearUserCascaron(request);

        User savedUser = userRepository.findById(userId).orElseThrow();
        assertEquals("teacher@test.com", savedUser.getEmail());
        assertNotNull(savedUser.getTeacherProfile());
        assertTrue(teacherProfileRepository.existsById(userId));
    }

    @Test
    void crearUserCascaron_Student_ShouldCreateUserAndStudentProfile() {
        UUID userId = UUID.randomUUID();
        AuthRegistrationRequest request = new AuthRegistrationRequest("student@test.com", userId, "ROLE_STUDENT");

        userService.crearUserCascaron(request);

        User savedUser = userRepository.findById(userId).orElseThrow();
        assertEquals("student@test.com", savedUser.getEmail());
        assertNotNull(savedUser.getStudentProfile());
        assertTrue(studentProfileRepository.existsById(userId));
    }

    @Test
    void completarPerfilBase_ShouldUpdateUserDetails() {
        UUID userId = UUID.randomUUID();
        userRepository.save(User.builder().id(userId).email("user@test.com").build());

        UserProfileCompletionRequest request = new UserProfileCompletionRequest(
                "12345678-9", "John", "Middle", "Doe", "Smith",
                "+56912345678", "Street 123", LocalDate.of(1990, 1, 1),
                "Chilena", masculino.getId()
        );

        userService.completarPerfilBase(userId, request);

        User updatedUser = userRepository.findById(userId).orElseThrow();
        assertEquals("12345678-9", updatedUser.getRut());
        assertEquals("John", updatedUser.getFirstName());
        assertEquals(masculino.getId(), updatedUser.getGender().getId());
    }

    @Test
    void actualizarPerfilEspecifico_Teacher_ShouldUpdateTeacherDetails() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).email("teacher@test.com").build();
        userRepository.save(user);
        teacherProfileRepository.save(TeacherProfile.builder().user(user).build());

        Map<String, Object> data = new HashMap<>();
        data.put("biography", "Expert in Java");
        data.put("office", "Room 101");
        data.put("availabilityHours", "9:00 - 17:00");
        data.put("academicGrade", "PhD");

        String token = generateMockToken("ROLE_TEACHER");
        userService.actualizarPerfilEspecifico(userId, token, data);

        TeacherProfile updatedProfile = teacherProfileRepository.findById(userId).orElseThrow();
        assertEquals("Expert in Java", updatedProfile.getBiography());
        assertEquals("PhD", updatedProfile.getAcademicGrade());
    }

    @Test
    void actualizarPerfilEspecifico_Student_ShouldUpdateStudentDetails() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).email("student@test.com").build();
        userRepository.save(user);
        studentProfileRepository.save(StudentProfile.builder().user(user).build());

        Map<String, Object> data = new HashMap<>();
        data.put("medicConditions", "None");

        String token = generateMockToken("ROLE_STUDENT");
        userService.actualizarPerfilEspecifico(userId, token, data);

        StudentProfile updatedProfile = studentProfileRepository.findById(userId).orElseThrow();
        assertEquals("None", updatedProfile.getMedicConditions());
    }

    @Test
    void actualizarPerfilEspecifico_Parent_ShouldUpdateParentDetails() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).email("parent@test.com").build();
        userRepository.save(user);
        // ParentProfileService.updateParentInfo creates profile if not exists, but let's be consistent
        parentProfileRepository.save(ParentProfile.builder().user(user).build());

        Map<String, Object> data = new HashMap<>();
        data.put("educationLevel", "University");
        data.put("isSupporter", true);

        String token = generateMockToken("ROLE_PARENT");
        userService.actualizarPerfilEspecifico(userId, token, data);

        ParentProfile updatedProfile = parentProfileRepository.findById(userId).orElseThrow();
        assertEquals("University", updatedProfile.getEducationLevel());
        assertTrue(updatedProfile.getIsSupporter());
    }

    @Test
    void actualizarPerfilEspecifico_RoleUser_ShouldThrowException() {
        UUID userId = UUID.randomUUID();
        String token = generateMockToken("ROLE_USER");
        Map<String, Object> data = new HashMap<>();

        assertThrows(RuntimeException.class, () -> 
            userService.actualizarPerfilEspecifico(userId, token, data)
        );
    }

    @Test
    void existeUsuario_ShouldReturnTrueIfUserExists() {
        UUID userId = UUID.randomUUID();
        userRepository.save(User.builder().id(userId).email("exists@test.com").build());

        assertTrue(userService.existeUsuario(userId));
    }

    @Test
    void existeUsuario_ShouldReturnFalseIfUserDoesNotExist() {
        assertFalse(userService.existeUsuario(UUID.randomUUID()));
    }

    private String generateMockToken(String role) {
        String header = Base64.getUrlEncoder().encodeToString("{\"alg\":\"HS256\"}".getBytes());
        String payload = Base64.getUrlEncoder().encodeToString(("{\"roles\":[\"" + role + "\"]}").getBytes());
        return "Bearer " + header + "." + payload + ".signature";
    }
}
