package com.promptlabs.usuarios_perfiles.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class TokenUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String getRoleFromToken(String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "").trim();

            String[] chunks = token.split("\\.");
            if (chunks.length < 2) throw new RuntimeException("Formato de token inválido");

            String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));

            Map<String, Object> payloadMap = objectMapper.readValue(payload, Map.class);

            Object rolesObj = payloadMap.get("roles");

            if (rolesObj instanceof List<?> rolesList && !rolesList.isEmpty()) {
                return rolesList.get(0).toString();
            } else if (rolesObj != null) {
                return rolesObj.toString();
            }

            throw new RuntimeException("No se encontró el rol en el token");

        } catch (Exception e) {
            System.err.println("Error decodificando token: " + e.getMessage());
            throw new RuntimeException("Token inválido o mal formado");

        }
    }
}