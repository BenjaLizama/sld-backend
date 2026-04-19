package com.promptlabs.autenticacion_seguridad.service;

import com.promptlabs.autenticacion_seguridad.dto.AuthResponse;
import com.promptlabs.autenticacion_seguridad.dto.RefreshTokenWrapper;
import com.promptlabs.autenticacion_seguridad.dto.SessionRequest;
import com.promptlabs.autenticacion_seguridad.dto.SessionResponse;

import java.util.UUID;

public interface ISessionService {
    SessionResponse createSession(UUID credentialId, SessionRequest sessionRequest);
    AuthResponse refreshToken(RefreshTokenWrapper wrapper);
    void deactivateSelf();
}