package com.promptlabs.autenticacion_seguridad.service;

import java.util.UUID;

public interface IRefreshTokenService {
    RefreshTokenEntity createRefreshEntity(UUID credentialId);
    RefreshTokenEntity verifyExpiration(RefreshTokenEntity token);
    void deleteByCredentialId(UUID credentialId);
}
