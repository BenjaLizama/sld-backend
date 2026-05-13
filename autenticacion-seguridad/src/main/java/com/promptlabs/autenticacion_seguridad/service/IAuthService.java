package com.promptlabs.autenticacion_seguridad.service;

import com.promptlabs.autenticacion_seguridad.dto.*;

public interface IAuthService {
    AuthResponse login(LoginWrapper wrapper);
    AuthResponse register(RegisterWrapper registerWrapper);
}