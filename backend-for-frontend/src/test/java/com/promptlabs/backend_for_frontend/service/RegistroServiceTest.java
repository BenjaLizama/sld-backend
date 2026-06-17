package com.promptlabs.backend_for_frontend.service;

import com.promptlabs.backend_for_frontend.dto.SuperRegistroDTO;
import com.promptlabs.backend_for_frontend.dto.UserResponse;
import com.promptlabs.backend_for_frontend.dto.UserInfoRequestDTO;
import com.promptlabs.backend_for_frontend.utils.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistroServiceTest {

    @Mock
    private AuthService authService;
    @Mock
    private UserService userService;
    @Mock
    private JwtService jwtService;

    private RegistroService registroService;

    @BeforeEach
    void setUp() {
        registroService = new RegistroService(authService, userService, jwtService);
    }

    @Test
    void registrar_ShouldOrchestrateSuccessfulRegistration() {
        // Arrange
        String deviceId = "device123";
        UUID userId = UUID.randomUUID();
        String token = "header.payload.signature";
        
        SuperRegistroDTO superRequest = mock(SuperRegistroDTO.class);
        UserInfoRequestDTO personalReq = mock(UserInfoRequestDTO.class);
        Map<String, Object> profileReq = Map.of("bio", "test bio");
        
        when(superRequest.personal()).thenReturn(personalReq);
        when(superRequest.profile()).thenReturn(profileReq);

        Map<String, Object> authRes = new HashMap<>();
        authRes.put("accessToken", token);
        when(authService.registrarUsuario(superRequest, deviceId)).thenReturn(authRes);

        Map<String, Object> claims = Map.of("userId", userId.toString());
        when(jwtService.extraerPayloadDelToken(token)).thenReturn(claims);
        when(jwtService.extraerRol(claims)).thenReturn("ROLE_STUDENT");

        UserResponse userRes = new UserResponse("John", "Doe", "john@test.com");
        when(userService.completarPerfilBase(userId, personalReq)).thenReturn(userRes);
        when(userService.completarPerfilEspecifico(eq("Bearer " + token), eq(userId), any())).thenReturn("Profile Linked");

        // Act
        Map<String, Object> result = registroService.registrar(superRequest, deviceId);

        // Assert
        assertNotNull(result);
        assertTrue((Boolean) result.get("perfilCompletado"));
        assertEquals("ROLE_STUDENT", result.get("rolAsignado"));
        assertEquals(userRes, result.get("userData"));
        assertEquals("Profile Linked", result.get("profileData"));
        
        verify(authService).registrarUsuario(superRequest, deviceId);
        verify(userService).completarPerfilBase(userId, personalReq);
        verify(userService).completarPerfilEspecifico(eq("Bearer " + token), eq(userId), any());
    }
}
