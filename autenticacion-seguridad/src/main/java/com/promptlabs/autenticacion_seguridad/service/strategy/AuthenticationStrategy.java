package com.promptlabs.autenticacion_seguridad.service.strategy;

import com.promptlabs.autenticacion_seguridad.dto.LoginRequest;
import com.promptlabs.autenticacion_seguridad.entity.CredentialEntity;
import com.promptlabs.autenticacion_seguridad.enums.LoginProvider;

public interface AuthenticationStrategy {
    LoginProvider getProvider();
    CredentialEntity authenticate(LoginRequest loginRequest);
}
