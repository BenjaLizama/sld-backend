package com.promptlabs.usuarios_perfiles.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptlabs.usuarios_perfiles.dto.UserProfileCompletionRequest;
import com.promptlabs.usuarios_perfiles.entity.Gender;
import com.promptlabs.usuarios_perfiles.entity.User;
import com.promptlabs.usuarios_perfiles.entity.TeacherProfile;
import com.promptlabs.usuarios_perfiles.repository.GenderRepository;
import com.promptlabs.usuarios_perfiles.repository.UserRepository;
import com.promptlabs.usuarios_perfiles.repository.TeacherProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Transactional
class UserProfileControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private org.springframework.web.context.WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private TeacherProfileRepository teacherProfileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID userId;
    private Gender masculino;

    @BeforeEach
    void setUp() {
        mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userId = UUID.randomUUID();
        masculino = genderRepository.findAll().stream()
                .filter(g -> g.getGender().equals("MASCULINO"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Gender MASCULINO not found in data.sql"));
        userRepository.save(User.builder().id(userId).email("user_test@test.com").build());
    }

    @Test
    void completeProfile_ShouldReturnOk() throws Exception {
        UserProfileCompletionRequest request = new UserProfileCompletionRequest(
                "12345678-9", "John", "Middle", "Doe", "Smith",
                "+56912345678", "Street 123", LocalDate.of(1990, 1, 1),
                "Chilena", masculino.getId()
        );

        mockMvc.perform(put("/api/v1/users/{userId}/complete-profile", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Perfil base actualizado con éxito"))
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    void updateProfile_Teacher_ShouldReturnOk() throws Exception {
        // Prepare user with teacher profile
        User user = userRepository.findById(userId).orElseThrow();
        teacherProfileRepository.save(TeacherProfile.builder().user(user).build());

        Map<String, Object> body = new HashMap<>();
        body.put("biography", "Expert Java Dev");
        body.put("academicGrade", "Master");

        String token = generateMockToken("ROLE_TEACHER");

        mockMvc.perform(put("/api/v1/users/{userId}/profile-update", userId)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Procesado correctamente"));
    }

    private String generateMockToken(String role) {
        String header = Base64.getUrlEncoder().encodeToString("{\"alg\":\"HS256\"}".getBytes());
        String payload = Base64.getUrlEncoder().encodeToString(("{\"roles\":[\"" + role + "\"]}").getBytes());
        return "Bearer " + header + "." + payload + ".signature";
    }
}
