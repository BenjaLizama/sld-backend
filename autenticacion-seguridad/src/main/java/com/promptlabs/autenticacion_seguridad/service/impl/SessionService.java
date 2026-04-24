package com.promptlabs.autenticacion_seguridad.service.impl;

import com.promptlabs.autenticacion_seguridad.dto.AuthResponse;
import com.promptlabs.autenticacion_seguridad.dto.ChangePasswordRequest;
import com.promptlabs.autenticacion_seguridad.dto.RefreshTokenWrapper;
import com.promptlabs.autenticacion_seguridad.dto.SessionRequest;
import com.promptlabs.autenticacion_seguridad.dto.SessionResponse;
import com.promptlabs.autenticacion_seguridad.entity.CredentialEntity;
import com.promptlabs.autenticacion_seguridad.entity.UserSessionEntity;
import com.promptlabs.autenticacion_seguridad.exception.CredentialNotFoundException;
import com.promptlabs.autenticacion_seguridad.exception.RefreshTokenNotFoundException;
import com.promptlabs.autenticacion_seguridad.repository.CredentialRepository;
import com.promptlabs.autenticacion_seguridad.repository.UserSessionRepository;
import com.promptlabs.autenticacion_seguridad.security.SecurityCredential;
import com.promptlabs.autenticacion_seguridad.service.ISessionService;
import com.promptlabs.autenticacion_seguridad.util.ClientContextHolder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService implements ISessionService {

    @Value("${jwt.expiration.refresh-token:2592000000}") // 30 días
    private Long refreshTokenDurationMs;

    private final UserSessionRepository userSessionRepository;
    private final CredentialRepository credentialRepository;
    private final TokenHashService tokenHashService;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final PasswordEncoder passwordEncoder;



    public void verifyExpiration(UserSessionEntity session) {
        if (session.getExpiryDate().isBefore(Instant.now())) {
            userSessionRepository.delete(session);
            throw new IllegalArgumentException("Sesión expirada cronológicamente.");
        }
        if (!session.getIsActive()) {
            userSessionRepository.delete(session);
            throw new IllegalArgumentException("Sesión revocada manualmente.");
        }
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenWrapper wrapper) {
        String rawToken = wrapper.token().requestToken().trim();
        String hashed = tokenHashService.hash(rawToken);

        UserSessionEntity session = userSessionRepository.findByRefreshTokenHash(hashed)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Sesión inválida o expirada."));

        verifyExpiration(session);

        if (!session.getDeviceId().equals(wrapper.session().deviceId())) {
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
        userSessionRepository.findByRefreshTokenHash(hashed)
                .ifPresent(userSessionRepository::delete);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        CredentialEntity credential = credentialRepository.findByEmail(email)
                .orElseThrow(() -> new CredentialNotFoundException("Usuario no encontrado."));

        if (!passwordEncoder.matches(request.oldPassword(), credential.getPassword())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta.");
        }

        if (passwordEncoder.matches(request.newPassword(), credential.getPassword())) {
            throw new IllegalArgumentException("La nueva contraseña no puede ser igual a la actual.");
        }

        credential.setPassword(passwordEncoder.encode(request.newPassword()));
        credentialRepository.saveAndFlush(credential); // HACE FLUSH INMEDIATO antes del clear, le dice cambia la contraseña altiro ahora
        userSessionRepository.revokeAllByCredentialId(credential.getId());

        log.info("[SECURITY] Contraseña actualizada para el usuario: {}. Sesiones revocadas.", email);
    }

    @Override
    @Transactional
    public void deactivateSelf() {
        // 1. Obtenemos el correo del usuario en base al contexto del token.
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        CredentialEntity credential = credentialRepository.findByEmail(email)
                .orElseThrow(() -> new CredentialNotFoundException("Usuario no encontrado."));

        // 2. Revocamos todas las sesiones (Utilizando el update masivo)
        userSessionRepository.revokeAllByCredentialId(credential.getId());

        // 3. Desactivamos la cuenta.
        credentialRepository.deactivateById(credential.getId());

        log.info("[SECURITY] Cuenta desactivada: {}. Trazabilidad temporal sincronizada.", email);
    }



    @Override
    @Transactional
    public void logout() {
        String accessToken = ClientContextHolder.getToken();
        String deviceId = ClientContextHolder.getDeviceId();

        if (accessToken == null || deviceId == null) {
            throw new SecurityException("Información de sesión incompleta para logout.");
        }

        // 1. Blacklist
        long ttlSeconds = jwtService.getRemainingTtlSeconds(accessToken);
        tokenBlacklistService.addToBlacklist(accessToken, ttlSeconds);

        // 2. Revocación Selectiva
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        CredentialEntity credential = credentialRepository.findByEmail(email)
                .orElseThrow(() -> new CredentialNotFoundException("Usuario no encontrado."));

        userSessionRepository.revokeByCredentialIdAndDeviceId(credential.getId(), deviceId);

        log.info("[LOGOUT] Sesión cerrada para el dispositivo: {}", deviceId);
    }


    @Override
    @Transactional
    public SessionResponse createSession(UUID credentialId, SessionRequest sessionRequest) {
        CredentialEntity credential = credentialRepository.findById(credentialId)
                .orElseThrow(() -> new CredentialNotFoundException("Credencial no encontrada."));

        // LEER SOLO DEL CONTEXTO (HEADER)
        String deviceId = ClientContextHolder.getDeviceId();

        if (deviceId == null || deviceId.isBlank()) {
            log.error("[SECURITY] Intento de creación de sesión sin X-Device-ID Header.");
            throw new IllegalArgumentException("El header X-Device-ID es obligatorio.");
        }

        // Limpiar sesión previa del mismo dispositivo
        userSessionRepository.findByCredentialAndDeviceId(credential, deviceId)
                .ifPresent(userSessionRepository::delete);

        userSessionRepository.flush();

        String rawToken = UUID.randomUUID().toString();
        String hashedToken = tokenHashService.hash(rawToken);

        UserSessionEntity session = UserSessionEntity.builder()
                .credential(credential)
                .refreshTokenHash(hashedToken)
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .deviceId(deviceId)
                .deviceName(sessionRequest.deviceName()) // El nombre (ej: "iPhone de Juan") sí puede venir del DTO
                .ipAddress(ClientContextHolder.getIp())
                .userAgent(sanitizeUserAgent(ClientContextHolder.getUserAgent()))
                .isActive(true)
                .build();

        userSessionRepository.save(session);
        return new SessionResponse(rawToken, session.getExpiryDate(), session.getDeviceName());
    }

    // acá
    private String sanitizeUserAgent(String ua) {
        if (ua == null) {
            return null;
        }
        // hace un control de caracteres y los saca
        String cleaned = ua.replaceAll("\\p{Cntrl}", "");
        // si encuentra 2 espacios los hace 1 y hace trim
        cleaned = cleaned.strip().replaceAll("\\s+", " ");
        // si después de normalizar queda vacío, viene vacío
        if (cleaned.isBlank()) {
            log.warn("[SECURITY] userAgent quedó vacío tras sanitización.");
            throw new IllegalArgumentException("El user agent no puede estar vacío.");
        }
        return cleaned;
    }


}
