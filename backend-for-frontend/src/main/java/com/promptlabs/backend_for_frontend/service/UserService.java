package com.promptlabs.backend_for_frontend.service;

import com.promptlabs.backend_for_frontend.client.UserClient;
import com.promptlabs.backend_for_frontend.dto.UserResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserClient userClient;
    @Retry(name = "user-service")
    @CircuitBreaker(
            name = "user-service",
            fallbackMethod = "fallbackPerfil")
    public UserResponse completarPerfilBase(UUID userId,Object personal){

        return userClient.actualizarInfoUsurio(userId,personal);

    }

    public String completarPerfilEspecifico(String bearerToken,UUID userID, Object profileData){
        return userClient.actualizarPerfilEspecifico(bearerToken,userID,profileData);
    }
    public UserResponse fallbackPerfil(
            UUID userId,
            Object personalData,
            Exception ex) {

        throw new RuntimeException(
                "Servicio User no disponible");
    }
}
