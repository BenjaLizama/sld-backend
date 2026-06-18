package com.promptlabs.backend_for_frontend.service;

import com.promptlabs.backend_for_frontend.client.AuthClient;
import com.promptlabs.backend_for_frontend.dto.AuthLoginResponse;
import com.promptlabs.backend_for_frontend.dto.LoginRequestDTO;
import com.promptlabs.backend_for_frontend.dto.SuperRegistroDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
@Slf4j
@Service
@AllArgsConstructor
public class AuthService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthService.class);
    private final AuthClient authClient;

    @Retry(name = "auth-service")
    @CircuitBreaker(
            name = "auth-service",
            fallbackMethod = "fallbackRegistrar")
    public Map<String, Object> registrarUsuario(
            SuperRegistroDTO superRequest, String headerDeviceId
    ) {
        log.info("Intentando registrar usuario para device: {}", headerDeviceId);
        Map<String, Object> bodyParaAuth = Map.of("register", superRequest.auth(), "session", superRequest.session());
        return authClient.registrarUsuario(headerDeviceId, bodyParaAuth);
    }

    public Map<String, Object> fallbackRegistrar(
            SuperRegistroDTO superRequest,
            String deviceId,
            Exception ex) {
        log.error("Fallback registrar activado para device {}. Error: {}", deviceId, ex.getMessage());
        throw new RuntimeException(
                "Servicio Auth no disponible");
    }

    @CircuitBreaker(
            name = "auth-service",
            fallbackMethod = "fallbackLogin")
    public AuthLoginResponse login(
            LoginRequestDTO request ,String headerDevice) {
        log.info("Intentando login para device: {}", headerDevice);
        return authClient.login(request,headerDevice);
    }

    public AuthLoginResponse fallbackLogin(
            LoginRequestDTO request,
            String headerDevice,
            Exception ex) {
        log.error("Fallback login activado para device {}. Error: {}", headerDevice, ex.getMessage());
        throw new RuntimeException(
                "Servicio de autenticación no disponible");
    }
}
