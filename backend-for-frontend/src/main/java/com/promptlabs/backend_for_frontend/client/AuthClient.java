package com.promptlabs.backend_for_frontend.client;

import com.promptlabs.backend_for_frontend.config.FeignConfig;
import com.promptlabs.backend_for_frontend.dto.AuthLoginResponse;
import com.promptlabs.backend_for_frontend.dto.LoginRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "auth-service", url = "${services.auth-service.url}",configuration = FeignConfig.class)
public interface AuthClient {
    @PostMapping("/api/v1/auth/register")
    Map<String, Object> registrarUsuario(
            @RequestHeader("X-Device-ID") String deviceId,
            @RequestBody Map<String, Object> datos // Aquí puedes dejarlo como Map si Auth espera el wrapper "register/session"
    );
    @PostMapping("/api/v1/auth/login")
    AuthLoginResponse login(
            @RequestBody LoginRequestDTO request,
            @RequestHeader("X-Device-ID") String deviceId
    );

}
