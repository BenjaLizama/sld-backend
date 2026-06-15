package com.promptlabs.usuarios_perfiles.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptlabs.usuarios_perfiles.dto.ParentInformationUpdateRequest;
import com.promptlabs.usuarios_perfiles.dto.StudentInformationUpdateRequest;
import com.promptlabs.usuarios_perfiles.dto.TeacherUpdateRequest;
import com.promptlabs.usuarios_perfiles.entity.User;
import com.promptlabs.usuarios_perfiles.entity.ParentProfile;
import com.promptlabs.usuarios_perfiles.entity.StudentProfile;
import com.promptlabs.usuarios_perfiles.entity.TeacherProfile;
import com.promptlabs.usuarios_perfiles.repository.UserRepository;
import com.promptlabs.usuarios_perfiles.repository.ParentProfileRepository;
import com.promptlabs.usuarios_perfiles.repository.StudentProfileRepository;
import com.promptlabs.usuarios_perfiles.repository.TeacherProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class ProfileControllersIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private org.springframework.web.context.WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParentProfileRepository parentProfileRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private TeacherProfileRepository teacherProfileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userId = UUID.randomUUID();
        user = User.builder().id(userId).email("profile_test@test.com").build();
        userRepository.save(user);
    }

    @Test
    void updateParentInfo_ShouldReturnOk() throws Exception {
        ParentInformationUpdateRequest request = new ParentInformationUpdateRequest("University", true);

        mockMvc.perform(put("/api/v1/parents/{userId}/parent-info", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Información de apoderado actualizada exitosamente"));
    }

    @Test
    void updateMedicalInfo_ShouldReturnOk() throws Exception {
        studentProfileRepository.save(StudentProfile.builder().user(user).build());
        StudentInformationUpdateRequest request = new StudentInformationUpdateRequest("None");

        mockMvc.perform(put("/api/v1/students/{userId}/medical-info", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Información médica actualizada exitosamente"));
    }

    @Test
    void updateTeacherInfo_ShouldReturnOk() throws Exception {
        teacherProfileRepository.save(TeacherProfile.builder().user(user).build());
        TeacherUpdateRequest request = new TeacherUpdateRequest("Bio", "Office", "Hours", "Grade");

        mockMvc.perform(put("/api/v1/teachers/{userId}/teacher-info", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Información de profesor actualizada exitosamente"));
    }
}
