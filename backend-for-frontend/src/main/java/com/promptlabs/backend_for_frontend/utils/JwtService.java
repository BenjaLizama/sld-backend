package com.promptlabs.backend_for_frontend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtService {


    public Map<String, Object> extraerPayloadDelToken(String token) {
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

    public String extraerRol(Map<String, Object> claims) {
        Object rolesObj = claims.get("roles");
        if (rolesObj instanceof java.util.List<?> list && !list.isEmpty()) {
            return list.get(0).toString().toUpperCase();
        }
        return rolesObj != null ? rolesObj.toString().toUpperCase() : "";
    }

    public UUID obtenerUserId(String token) {

        Map<String,Object> claims =
                extraerPayloadDelToken(token);

        return UUID.fromString(
                claims.get("userId").toString());
    }
    public String obtenerRol(String token) {

        Map<String,Object> claims =
                extraerPayloadDelToken(token);

        return extraerRol(claims);
    }
}
