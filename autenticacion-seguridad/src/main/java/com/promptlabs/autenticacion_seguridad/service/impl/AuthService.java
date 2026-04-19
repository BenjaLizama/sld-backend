package com.promptlabs.autenticacion_seguridad.service.impl;

import com.promptlabs.autenticacion_seguridad.dto.AuthResponse;
import com.promptlabs.autenticacion_seguridad.dto.LoginRequest;
import com.promptlabs.autenticacion_seguridad.dto.RegisterRequest;
import com.promptlabs.autenticacion_seguridad.entity.CredentialEntity;
import com.promptlabs.autenticacion_seguridad.entity.RoleEntity;
import com.promptlabs.autenticacion_seguridad.exception.EmailAlreadyExistsException;
import com.promptlabs.autenticacion_seguridad.exception.RefreshTokenNotFoundException;
import com.promptlabs.autenticacion_seguridad.exception.RoleNotFoundException;
import com.promptlabs.autenticacion_seguridad.repository.CredentialRepository;
import com.promptlabs.autenticacion_seguridad.repository.RoleRepository;
import com.promptlabs.autenticacion_seguridad.security.SecurityCredential;
import com.promptlabs.autenticacion_seguridad.service.IAuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Inicio de sesión
     * @param request Recibe correo y contraseña.
     * @return Token de acceso y refreshToken.
     */
    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        SecurityCredential user = (SecurityCredential) userDetailsService.loadUserByUsername(request.email());

        String accessToken = jwtService.generateToken(user);
        // Creamos el refresh token en la base de datos vinculado al UUID
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshEntity(user.getId());

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    /**
     * Registro de usuario
     * @param request Correo y contraseña.
     * @return Token de acceso.
     */
    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Validar si el correo existe en la BD.
        if (credentialRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("El correo electrónico ya está registrado en el sistema");
        }

        // Buscar el rol por defecto.
        RoleEntity defaultRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("Error crítico: El rol ROLE_USER no existe en la base de datos."));

        // Crear y guardar la credencial.
        CredentialEntity credential = new CredentialEntity();
        credential.setEmail(request.email());
        credential.setPassword(passwordEncoder.encode(request.password()));
        credential.setIsValid(true);
        credential.setRoleList(Set.of(defaultRole));

        // Guardamos primero para que el ID sea generado y persistido.
        CredentialEntity savedCredential = credentialRepository.save(credential);

        // Preparamos la respuesta completa.
        // Usamos el savedCredential para asegurar que tenemos el UUID disponible
        SecurityCredential userDetails = new SecurityCredential(savedCredential);

        String accessToken = jwtService.generateToken(userDetails);
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshEntity(savedCredential.getId());

        // Devolvemos ambos tokens.
        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(String requestToken) {
        return refreshTokenRepository.findByToken(requestToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshTokenEntity::getCredential)
                .map(credential -> {
                   UserDetails userDetails = new SecurityCredential(credential);
                   String accessToken = jwtService.generateToken(userDetails);

                   // "ROTACIÓN" | Creamos un nuevo token y el service elimina el anterior.
                    RefreshTokenEntity newRefreshToken = refreshTokenService.createRefreshEntity(credential.getId());

                   return new AuthResponse(accessToken, newRefreshToken.getToken());
                })
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token no encontrado."));
    }

}
