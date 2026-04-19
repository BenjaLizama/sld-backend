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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final SessionService sessionService;

    /**
     * Inicio de sesión
     */
    @Override
    @Transactional
    public AuthResponse login(LoginWrapper wrapper) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(wrapper.login().email(), wrapper.login().password())
        );

        SecurityCredential user = (SecurityCredential) userDetailsService.loadUserByUsername(wrapper.login().email());

        if (!user.isEnabled()) {
            throw new DisabledException("La cuenta se encuentra desactivada.");
        }

        String accessToken = jwtService.generateToken(user);

        // Crear sesión multidispositivo
        SessionResponse session = sessionService.createSession(user.getId(), wrapper.session());

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
