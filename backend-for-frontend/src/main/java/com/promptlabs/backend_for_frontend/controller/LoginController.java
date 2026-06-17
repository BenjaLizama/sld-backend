package com.promptlabs.backend_for_frontend.controller;

import com.promptlabs.backend_for_frontend.dto.AuthLoginResponse;
import com.promptlabs.backend_for_frontend.dto.LoginRequestDTO;
import com.promptlabs.backend_for_frontend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LoginController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(
            @RequestHeader(value = "X-Device-ID", required = false) String deviceId,
            @RequestBody LoginRequestDTO request) {

        return ResponseEntity.ok(
                authService.login(request,deviceId));
    }
}
