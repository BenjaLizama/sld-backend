package com.promptlabs.autenticacion_seguridad.service.impl;

import com.promptlabs.autenticacion_seguridad.dto.AuthResponse;
import com.promptlabs.autenticacion_seguridad.dto.RefreshTokenWrapper;
import com.promptlabs.autenticacion_seguridad.dto.SessionRequest;
import com.promptlabs.autenticacion_seguridad.dto.SessionResponse;
import com.promptlabs.autenticacion_seguridad.entity.CredentialEntity;
import com.promptlabs.autenticacion_seguridad.entity.SessionEntity;
import com.promptlabs.autenticacion_seguridad.exception.CredentialNotFoundException;
import com.promptlabs.autenticacion_seguridad.exception.RefreshTokenNotFoundException;
import com.promptlabs.autenticacion_seguridad.repository.CredentialRepository;
import com.promptlabs.autenticacion_seguridad.repository.SessionsRepository;
import com.promptlabs.autenticacion_seguridad.security.SecurityCredential;
import com.promptlabs.autenticacion_seguridad.service.ISessionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class SessionService implements ISessionService {

    @Value("${jwt.expiration.refresh-token:2592000000}") // 30 días
    private Long refreshTokenDurationMs;

    private final SessionsRepository sessionsRepository;
    private final CredentialRepository credentialRepository;
    private final TokenHashService tokenHashService;
    private final JwtService jwtService;

    @Override
    @Transactional
    public SessionResponse createSession(UUID credentialId, SessionRequest sessionRequest) {
        CredentialEntity credential = credentialRepository.findById(credentialId)
                .orElseThrow(() -> new CredentialNotFoundException("Credencial no encontrada."));

        sessionsRepository.findByCredentialAndDeviceId(credential, sessionRequest.deviceId())
                .ifPresent(sessionsRepository::delete);

        sessionsRepository.flush();

        String rawToken = UUID.randomUUID().toString();
        String hashedToken = tokenHashService.hash(rawToken);

        SessionEntity session = SessionEntity.builder()
                .credential(credential)
                .refreshTokenHash(hashedToken)
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .deviceId(sessionRequest.deviceId())
                .deviceName(sessionRequest.deviceName())
                .ipAddress(sessionRequest.ipAddress())
                .userAgent(sessionRequest.ua())
                .isActive(true)
                .build();

        sessionsRepository.save(session);

        return new SessionResponse(rawToken, session.getExpiryDate(), session.getDeviceName());
    }

    public void verifyExpiration(SessionEntity session) {
        if (session.getExpiryDate().isBefore(Instant.now())) {
            sessionsRepository.delete(session);
            throw new IllegalArgumentException("Sesión expirada cronológicamente.");
        }
        if (!session.getIsActive()) {
            sessionsRepository.delete(session);
            throw new IllegalArgumentException("Sesión revocada manualmente.");
        }
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenWrapper wrapper) {
        // Limpiamos cualquier salgo de linea invisible.
        String rawToken = wrapper.token().requestToken().trim();
        String hashed = tokenHashService.hash(rawToken);

        System.out.println("DEBUG - Token Plano: " + rawToken);
        System.out.println("DEBUG - Hash Generado: " + hashed);

        SessionEntity session = sessionsRepository.findByRefreshTokenHash(hashed)
                .orElseThrow(() -> {
                    System.err.println("ERROR: No se encontró el hash " + hashed);
                    return new RefreshTokenNotFoundException("Sesión inválida o expirada.");
                });

        verifyExpiration(session);

        if (!session.getDeviceId().equals(wrapper.session().deviceId())) {
            // Esto previene que alguien robe un refresh token y lo use en otro dispositivo
            throw new SecurityException("El dispositivo no coincide con la sesión iniciada.");
        }

        SecurityCredential userDetails = new SecurityCredential(session.getCredential());
        String newAccessToken = jwtService.generateToken(userDetails);

        // Rotación de sesión
        SessionResponse newSession = createSession(
                session.getCredential().getId(),
                wrapper.session()
        );

        return new AuthResponse(newAccessToken, newSession.rawRefreshToken(), newSession.expiryDate());
    }

    @Transactional
    public void revokeSession(String rawRefreshToken) {
        String hashed = tokenHashService.hash(rawRefreshToken);
        sessionsRepository.findByRefreshTokenHash(hashed)
                .ifPresent(sessionsRepository::delete);
    }

    @Override
    @Transactional
    public void deactivateSelf() {
        // 1. Obtenemos el correo del usuario en base al contexto del token.
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        CredentialEntity credential = credentialRepository.findByEmail(email)
                .orElseThrow(() -> new CredentialNotFoundException("Usuario no encontrado."));

        UUID credentialId = credential.getId();
        sessionsRepository.revokeAllByCredentialId(credentialId);

        credential.setIsActive(false);
        credential.setDeactivatedAt(Instant.now());

        credentialRepository.save(credential);
    }

}
