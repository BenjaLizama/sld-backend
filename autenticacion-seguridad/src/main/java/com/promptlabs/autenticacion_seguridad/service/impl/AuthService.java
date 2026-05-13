package com.promptlabs.autenticacion_seguridad.service.impl;

import com.promptlabs.autenticacion_seguridad.dto.*;
import com.promptlabs.autenticacion_seguridad.entity.CredentialEntity;
import com.promptlabs.autenticacion_seguridad.entity.RoleEntity;
import com.promptlabs.autenticacion_seguridad.exception.EmailAlreadyExistsException;
import com.promptlabs.autenticacion_seguridad.exception.RoleNotFoundException;
import com.promptlabs.autenticacion_seguridad.repository.CredentialRepository;
import com.promptlabs.autenticacion_seguridad.repository.RoleRepository;
import com.promptlabs.autenticacion_seguridad.security.SecurityCredential;
import com.promptlabs.autenticacion_seguridad.service.IAuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final CredentialRepository credentialRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthStrategyManager authStrategyManager;
    private final SessionService sessionService;

    /**
     * Inicio de sesión
     */
    @Override
    @Transactional
    public AuthResponse login(LoginWrapper wrapper) {
        // 1. La estrategia valida según el provider y nos da la entidad
        CredentialEntity credential = authStrategyManager.executeStrategy(wrapper.login());

        // 2. Convertimos a SecurityCredential para el JWT (como ya lo hacías)
        SecurityCredential userDetails = new SecurityCredential(credential);

        // 3. Generamos tokens y sesión
        String accessToken = jwtService.generateToken(userDetails);
        SessionResponse session = sessionService.createSession(credential.getId(), wrapper.session());

        return new AuthResponse(accessToken, session.rawRefreshToken(), session.expiryDate());
    }

    /**
     * Registro de usuario
     */
    @Override
    @Transactional
    public AuthResponse register(RegisterWrapper registerWrapper) {
        if (credentialRepository.existsByEmail(registerWrapper.register().email())) {
            throw new EmailAlreadyExistsException("El correo electrónico ya está registrado");
        }

        RoleEntity defaultRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("Error crítico: ROLE_USER no existe."));

        CredentialEntity credential = new CredentialEntity();
        credential.setEmail(registerWrapper.register().email());
        credential.setPassword(passwordEncoder.encode(registerWrapper.register().password()));
        credential.setIsActive(true);
        credential.setRoleList(Set.of(defaultRole));

        CredentialEntity savedCredential = credentialRepository.save(credential);
        SecurityCredential userDetails = new SecurityCredential(savedCredential);

        String accessToken = jwtService.generateToken(userDetails);

        // Crear nueva sesión
        SessionResponse session = sessionService.createSession(savedCredential.getId(), registerWrapper.session());

        return new AuthResponse(accessToken, session.rawRefreshToken(), session.expiryDate());
    }

}
