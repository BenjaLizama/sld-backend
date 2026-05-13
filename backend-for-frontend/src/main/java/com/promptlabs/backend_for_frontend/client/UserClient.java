package com.promptlabs.backend_for_frontend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "user-service", url = "${services.user-service.url}")
public interface UserClient {

    @PutMapping("/api/v1/users/{userId}/complete-profile")
    void actualizarInfoUsurio(
            @PathVariable("userId") UUID userId,
            @RequestBody Object userInfoRequest
    );

    @PutMapping("/api/v1/users/{userId}/profile-update")
    void actualizarPerfilEspecifico(
            @RequestHeader("Authorization") String token,
            @PathVariable("userId") UUID userId,
            @RequestBody Object body
    );
}