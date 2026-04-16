package com.promptlabs.autenticacion_seguridad.service;

import com.promptlabs.autenticacion_seguridad.dto.AuthResponse;
import com.promptlabs.autenticacion_seguridad.dto.LoginRequest;
import com.promptlabs.autenticacion_seguridad.dto.RegisterRequest;

public interface IAuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
    AuthResponse refreshToken(String requestToken);
}
