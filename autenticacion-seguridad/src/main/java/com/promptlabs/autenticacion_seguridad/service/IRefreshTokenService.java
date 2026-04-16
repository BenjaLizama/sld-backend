package com.promptlabs.autenticacion_seguridad.service;

import com.promptlabs.autenticacion_seguridad.entity.RefreshTokenEntity;

import java.util.UUID;

public interface IRefreshTokenService {
    RefreshTokenEntity createRefreshEntity(UUID credentialId);
    RefreshTokenEntity verifyExpiration(RefreshTokenEntity token);
    void deleteByCredentialId(UUID credentialId);
}
