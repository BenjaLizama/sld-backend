package com.promptlabs.autenticacion_seguridad.service.impl;

import com.promptlabs.autenticacion_seguridad.entity.CredentialEntity;
import com.promptlabs.autenticacion_seguridad.repository.CredentialRepository;
import com.promptlabs.autenticacion_seguridad.service.IRefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements IRefreshTokenService {

    @Value("${jwt.expiration.refresh-token:2592000000}") // 30 días
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final CredentialRepository credentialRepository;

    @Override
    public RefreshTokenEntity createRefreshEntity(UUID credentialId) {
        CredentialEntity credential = credentialRepository.findById(credentialId)
                .orElseThrow(() -> new IllegalArgumentException("Credenecial no encontrada.")); // TODO: EXCEPCIÓN PERSONALIZADA AQUÍ.

        refreshTokenRepository.deleteByCredential(credential);

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .credential(credential)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .isValid(true)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new IllegalArgumentException("El refresh token ha expirado. Por favor, inicie sesión nuevamente.");
        }
        return token;
    }

    @Override
    @Transactional
    public void deleteByCredentialId(UUID credentialId) {
        credentialRepository.findById(credentialId)
                .ifPresent(refreshTokenRepository::deleteByCredential);
    }

}
