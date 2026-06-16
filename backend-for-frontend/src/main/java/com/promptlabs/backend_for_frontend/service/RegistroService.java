package com.promptlabs.backend_for_frontend.service;

import com.promptlabs.backend_for_frontend.client.AuthClient;
import com.promptlabs.backend_for_frontend.client.UserClient;
import com.promptlabs.backend_for_frontend.dto.SuperRegistroDTO;
import com.promptlabs.backend_for_frontend.dto.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RegistroService {
    private final AuthClient authClient;
    private final UserClient userClient;

    public Map<String, Object> registrar(SuperRegistroDTO superRequest, String headerDeviceId) {
        // 1. Llamada a Auth
        // Cambiamos el Map.of por un HashMap amigable para Feign/Jackson
        Map<String, Object> bodyParaAuth = new java.util.HashMap<>();
        bodyParaAuth.put("register", superRequest.auth());
        bodyParaAuth.put("session", superRequest.session());

        Map<String, Object> authRes = authClient.registrarUsuario(headerDeviceId, bodyParaAuth);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String token = (String) authRes.get("accessToken");
        String bearerToken = "Bearer " + token;

        Map<String, Object> claims = extraerPayloadDelToken(token);
        UUID userId = UUID.fromString((String) claims.get("userId"));
        String role = extraerRol(claims);

        // --- MODIFICACIÓN: Capturar respuestas de Usuarios ---

        // 2. Actualizar Info Personal y capturar respuesta
        UserResponse userRes = userClient.actualizarInfoUsurio(userId, superRequest.personal());

        // 3. Actualizar Perfil Específico y capturar respuesta (si aplica)
        String profileRes = null;
        if (superRequest.profile() != null) {
            profileRes = userClient.actualizarPerfilEspecifico(bearerToken, userId, superRequest.profile());
        }

        // 4. Unificar todo en la respuesta final
        Map<String, Object> resFinal = new java.util.HashMap<>(authRes);
        resFinal.put("perfilCompletado", true);
        resFinal.put("rolAsignado", role);
        resFinal.put("userData", userRes); // Respuesta de completarPerfilBase

        if (profileRes != null) {
            resFinal.put("profileData", profileRes); // Respuesta de actualizarPerfilEspecifico
        }

        return resFinal;
    }

    private Map<String, Object> extraerPayloadDelToken(String token) {
        try {
            String[] chunks = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));
            return new ObjectMapper()
                    .findAndRegisterModules()
                    .readValue(payload, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al decodificar el token: " + e.getMessage());
        }
    }

    private String extraerRol(Map<String, Object> claims) {
        Object rolesObj = claims.get("roles");
        if (rolesObj instanceof java.util.List<?> list && !list.isEmpty()) {
            return list.get(0).toString().toUpperCase();
        }
        return rolesObj != null ? rolesObj.toString().toUpperCase() : "";
    }
}