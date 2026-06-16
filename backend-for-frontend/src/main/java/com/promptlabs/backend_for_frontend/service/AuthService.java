package com.promptlabs.backend_for_frontend.service;

import com.promptlabs.backend_for_frontend.client.AuthClient;
import com.promptlabs.backend_for_frontend.dto.AuthLoginResponse;
import com.promptlabs.backend_for_frontend.dto.LoginRequestDTO;
import com.promptlabs.backend_for_frontend.dto.SuperRegistroDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthClient authClient;

    @Retry(name = "auth-service")
    @CircuitBreaker(
            name = "auth-service",
            fallbackMethod = "fallbackRegistrar")
    public Map<String, Object> registrarUsuario(
            SuperRegistroDTO superRequest, String headerDeviceId
    ) {

        Map<String, Object> bodyParaAuth = Map.of("register", superRequest.auth(), "session", superRequest.session());
        return authClient.registrarUsuario(headerDeviceId, bodyParaAuth);
    }

    public Map<String, Object> fallbackRegistrar(
            SuperRegistroDTO superRequest,
            String deviceId,
            Exception ex) {

        throw new RuntimeException(
                "Servicio Auth no disponible");
    }

    @CircuitBreaker(
            name = "auth-service",
            fallbackMethod = "fallbackLogin")
    public AuthLoginResponse login(
            LoginRequestDTO request ,String headerDevice) {

        return authClient.login(request,headerDevice);
    }

    public AuthLoginResponse fallbackLogin(
            LoginRequestDTO request,
            Exception ex) {

        throw new RuntimeException(
                "Servicio de autenticación no disponible");
    }
}
