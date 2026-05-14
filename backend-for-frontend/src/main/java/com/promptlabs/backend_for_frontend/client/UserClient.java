package com.promptlabs.backend_for_frontend.client;

import com.promptlabs.backend_for_frontend.config.FeignConfig;
import com.promptlabs.backend_for_frontend.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "user-service", url = "${services.user-service.url}",configuration = FeignConfig.class)
public interface UserClient {

    @PutMapping("/api/v1/users/{userId}/complete-profile")
    UserResponse actualizarInfoUsurio(@PathVariable UUID userId, @RequestBody Object body);

    @PutMapping("/api/v1/users/{userId}/profile-update")
    String actualizarPerfilEspecifico(
            @RequestHeader("Authorization") String token,
            @PathVariable("userId") UUID userId,
            @RequestBody Object body
    );
}