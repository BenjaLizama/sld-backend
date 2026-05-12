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
        try {
            Map<String, Object> bodyParaAuth = Map.of(
                    "register", superRequest.auth(),
                    "session", superRequest.session()
            );

            String finalDeviceId = (superRequest.session().deviceId() != null)
                    ? superRequest.session().deviceId() : headerDeviceId;

            Map<String, Object> authRes = authClient.registrarUsuario(finalDeviceId, bodyParaAuth);
            String token = (String) authRes.get("accessToken");
            String userIdStr = extraerUserIdDelToken(token);
            UUID userId = UUID.fromString(userIdStr);

            if (superRequest.profile() != null) {
                Thread.sleep(500);

                userClient.actualizarInfoUsurio(userId, superRequest.personal());

                System.out.println("Actualizando perfil con Record para ID: " + userId);
                userClient.actualizarInfoApoderado(userId, superRequest.profile());
            }

            return authRes;

        } catch (Exception e) {
            System.err.println("ERROR CON RECORDS: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
    private String extraerUserIdDelToken(String token) {
        try {
            String[] chunks = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));
            Map<String, Object> payloadMap = new ObjectMapper().readValue(payload, Map.class);
            return (String) payloadMap.get("userId");
        } catch (Exception e) {
            throw new RuntimeException("Error al decodificar el token para obtener el ID");
        }
    }
}
