package com.promptlabs.backend_for_frontend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        objectMapper = new ObjectMapper();
    }

    private String generateMockToken(Map<String, Object> payloadMap) throws Exception {
        String header = Base64.getUrlEncoder().encodeToString("{\"alg\":\"HS256\"}".getBytes());
        String payload = Base64.getUrlEncoder().encodeToString(objectMapper.writeValueAsString(payloadMap).getBytes());
        return header + "." + payload + ".signature";
    }

    @Test
    void extraerPayloadDelToken_ShouldReturnCorrectMap() throws Exception {
        UUID userId = UUID.randomUUID();
        Map<String, Object> payload = Map.of("userId", userId.toString(), "roles", List.of("ROLE_USER"));
        String token = generateMockToken(payload);

        Map<String, Object> result = jwtService.extraerPayloadDelToken(token);

        assertEquals(userId.toString(), result.get("userId"));
        assertEquals(List.of("ROLE_USER"), result.get("roles"));
    }

    @Test
    void extraerRol_ShouldReturnUppercaseRole() {
        Map<String, Object> claims = Map.of("roles", List.of("role_student"));
        String rol = jwtService.extraerRol(claims);
        assertEquals("ROLE_STUDENT", rol);
    }

    @Test
    void extraerRol_ShouldReturnUppercaseStringRole() {
        Map<String, Object> claims = Map.of("roles", "role_admin");
        String rol = jwtService.extraerRol(claims);
        assertEquals("ROLE_ADMIN", rol);
    }

    @Test
    void obtenerUserId_ShouldReturnUuid() throws Exception {
        UUID userId = UUID.randomUUID();
        Map<String, Object> payload = Map.of("userId", userId.toString());
        String token = generateMockToken(payload);

        UUID result = jwtService.obtenerUserId(token);

        assertEquals(userId, result);
    }

    @Test
    void extraerPayloadDelToken_ShouldThrowExceptionOnInvalidToken() {
        assertThrows(RuntimeException.class, () -> jwtService.extraerPayloadDelToken("invalidtoken"));
    }
}
