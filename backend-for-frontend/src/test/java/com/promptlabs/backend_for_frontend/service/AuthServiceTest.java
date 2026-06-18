package com.promptlabs.backend_for_frontend.service;

import com.promptlabs.backend_for_frontend.client.AuthClient;
import com.promptlabs.backend_for_frontend.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthClient authClient;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(authClient);
    }

    @Test
    void registrarUsuario_ShouldCallAuthClient() {
        // Mock data
        SuperRegistroDTO superRequest = mock(SuperRegistroDTO.class);
        AuthRequestDTO authRequest = new AuthRequestDTO("test@test.com", "Pass123!", "ROLE_USER");
        SessionDTO sessionRequest = new SessionDTO("dev123", "Chrome");
        
        when(superRequest.auth()).thenReturn(authRequest);
        when(superRequest.session()).thenReturn(sessionRequest);
        
        String deviceId = "dev123";
        Map<String, Object> expectedResponse = Map.of("status", "success");

        when(authClient.registrarUsuario(eq(deviceId), any())).thenReturn(expectedResponse);

        // Execute
        Map<String, Object> result = authService.registrarUsuario(superRequest, deviceId);

        // Verify
        assertNotNull(result);
        assertEquals("success", result.get("status"));
        verify(authClient).registrarUsuario(eq(deviceId), any());
    }

    @Test
    void login_ShouldCallAuthClient() {
        // Mock data
        LoginData loginData = new LoginData("user", "pass", "LOCAL");
        SessionDTO sessionData = new SessionDTO("dev123", "Chrome");
        LoginRequestDTO request = new LoginRequestDTO(loginData, sessionData);
        String deviceId = "dev123";
        AuthLoginResponse expectedResponse = new AuthLoginResponse("token", "refreshToken", null);

        when(authClient.login(request, deviceId)).thenReturn(expectedResponse);

        // Execute
        AuthLoginResponse result = authService.login(request, deviceId);

        // Verify
        assertNotNull(result);
        assertEquals("token", result.accessToken());
        verify(authClient).login(request, deviceId);
    }

    @Test
    void fallbackLogin_ShouldThrowRuntimeException() {
        LoginData loginData = new LoginData("user", "pass", "LOCAL");
        SessionDTO sessionData = new SessionDTO("dev123", "Chrome");
        LoginRequestDTO request = new LoginRequestDTO(loginData, sessionData);
        String deviceId = "dev123";
        Exception ex = new Exception("Service Down");

        assertThrows(RuntimeException.class, () -> authService.fallbackLogin(request, deviceId, ex));
    }

    @Test
    void fallbackRegistrar_ShouldThrowRuntimeException() {
        SuperRegistroDTO superRequest = mock(SuperRegistroDTO.class);
        String deviceId = "dev123";
        Exception ex = new Exception("Service Down");

        assertThrows(RuntimeException.class, () -> authService.fallbackRegistrar(superRequest, deviceId, ex));
    }
}
