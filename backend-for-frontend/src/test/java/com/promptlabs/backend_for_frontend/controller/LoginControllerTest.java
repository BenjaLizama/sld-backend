package com.promptlabs.backend_for_frontend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptlabs.backend_for_frontend.dto.AuthLoginResponse;
import com.promptlabs.backend_for_frontend.dto.LoginData;
import com.promptlabs.backend_for_frontend.dto.LoginRequestDTO;
import com.promptlabs.backend_for_frontend.dto.SessionDTO;
import com.promptlabs.backend_for_frontend.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private LoginController loginController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    void login_ShouldReturnOk() throws Exception {
        LoginData loginData = new LoginData("user", "pass", "LOCAL");
        SessionDTO sessionData = new SessionDTO("device-123", "Chrome");
        LoginRequestDTO request = new LoginRequestDTO(loginData, sessionData);
        AuthLoginResponse response = new AuthLoginResponse("access-token", "refresh-token", null);

        when(authService.login(any(LoginRequestDTO.class), eq("device-123"))).thenReturn(response);

        mockMvc.perform(post("/api/v1/login/login")
                        .header("X-Device-ID", "device-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }
}
