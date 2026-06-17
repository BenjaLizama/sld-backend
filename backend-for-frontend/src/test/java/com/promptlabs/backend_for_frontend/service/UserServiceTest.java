package com.promptlabs.backend_for_frontend.service;

import com.promptlabs.backend_for_frontend.client.UserClient;
import com.promptlabs.backend_for_frontend.dto.UserResponse;
import com.promptlabs.backend_for_frontend.dto.UserSummaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserClient userClient;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userClient);
    }

    @Test
    void completarPerfilBase_ShouldCallUserClient() {
        UUID userId = UUID.randomUUID();
        Object personalData = new Object();
        UserResponse expectedResponse = new UserResponse("John Doe", "12345678-9", "Success");

        when(userClient.actualizarInfoUsurio(userId, personalData)).thenReturn(expectedResponse);

        UserResponse result = userService.completarPerfilBase(userId, personalData);

        assertNotNull(result);
        assertEquals("John Doe", result.name());
        verify(userClient).actualizarInfoUsurio(userId, personalData);
    }

    @Test
    void completarPerfilEspecifico_ShouldCallUserClient() {
        String token = "Bearer token";
        UUID userId = UUID.randomUUID();
        Object profileData = new Object();
        String expectedResponse = "Success";

        when(userClient.actualizarPerfilEspecifico(token, userId, profileData)).thenReturn(expectedResponse);

        String result = userService.completarPerfilEspecifico(token, userId, profileData);

        assertEquals("Success", result);
        verify(userClient).actualizarPerfilEspecifico(token, userId, profileData);
    }

    @Test
    void listUsers_ShouldReturnListFromClient() {
        List<UserSummaryResponse> expectedList = List.of(
                new UserSummaryResponse("John Doe", "john@test.com", "Address", null, "Nationality", "Gender", "Rut", null, "12345")
        );

        when(userClient.listUsers()).thenReturn(expectedList);

        List<UserSummaryResponse> result = userService.listUsers();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).fullName());
        verify(userClient).listUsers();
    }

    @Test
    void fallbackUsers_ShouldReturnEmptyList() {
        List<UserSummaryResponse> result = userService.fallbackUsers(new Exception("Error"));
        assertTrue(result.isEmpty());
    }

    @Test
    void fallbackPerfil_ShouldThrowRuntimeException() {
        UUID userId = UUID.randomUUID();
        Object data = new Object();
        assertThrows(RuntimeException.class, () -> userService.fallbackPerfil(userId, data, new Exception("Error")));
    }
}
