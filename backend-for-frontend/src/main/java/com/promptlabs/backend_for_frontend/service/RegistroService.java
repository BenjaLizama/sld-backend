package com.promptlabs.backend_for_frontend.service;

import com.promptlabs.backend_for_frontend.client.AuthClient;
import com.promptlabs.backend_for_frontend.client.UserClient;
import com.promptlabs.backend_for_frontend.dto.SuperRegistroDTO;
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
        Map<String, Object> bodyParaAuth = Map.of(
                "register", superRequest.auth(),
                "session", superRequest.session()
        );

        Map<String, Object> authRes = authClient.registrarUsuario(headerDeviceId, bodyParaAuth);
        String token = (String) authRes.get("accessToken");
        String bearerToken = "Bearer " + token;

        Map<String, Object> claims = extraerPayloadDelToken(token);
        UUID userId = UUID.fromString((String) claims.get("userId"));
        String role = extraerRol(claims);

        userClient.actualizarInfoUsurio(userId, superRequest.personal());

        if (superRequest.profile() != null) {
            userClient.actualizarPerfilEspecifico(bearerToken, userId, superRequest.profile());
        }

        Map<String, Object> resFinal = new java.util.HashMap<>(authRes);
        resFinal.put("perfilCompletado", true);
        resFinal.put("rolAsignado", role);

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