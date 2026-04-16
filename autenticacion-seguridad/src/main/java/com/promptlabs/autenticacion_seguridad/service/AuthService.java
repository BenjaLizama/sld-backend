package com.promptlabs.autenticacion_seguridad.service;

import com.promptlabs.autenticacion_seguridad.dto.AuthResponse;
import com.promptlabs.autenticacion_seguridad.dto.LoginRequest;
import com.promptlabs.autenticacion_seguridad.dto.RegisterRequest;
import com.promptlabs.autenticacion_seguridad.entity.CredentialEntity;
import com.promptlabs.autenticacion_seguridad.entity.RoleEntity;
import com.promptlabs.autenticacion_seguridad.repository.CredentialRepository;
import com.promptlabs.autenticacion_seguridad.repository.RoleRepository;
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
public class AuthService {

    private final CredentialRepository credentialRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Inicio de sesión
     * @param request Recibe correo y contraseña.
     * @return Token de acceso.
     */
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserDetails user = userDetailsService.loadUserByUsername(request.email());

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }

    /**
     * Registro de usuario
     * @param request Correo y contraseña.
     * @return Token de acceso.
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // TODO: Validar que el email no exista en la BD (COMPLETADO)
        if (credentialRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado en el sistema"); // TODO: CREAR EXCEPCIÓN PERSONALIZADA.
        }

        RoleEntity defaultRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error crítico: El rol ROLE_USER no existe en la base de datos.")); // TODO: CREAR EXCEPCIÓN PERSONALIZADA.

        CredentialEntity credential = new CredentialEntity();
        credential.setEmail(request.email());
        credential.setPassword(passwordEncoder.encode(request.password()));
        credential.setIsValid(true);
        // TODO: Asignar un rol por defecto
        credential.setRoleList(Set.of(defaultRole));

        credentialRepository.save(credential);

        UserDetails user = userDetailsService.loadUserByUsername(request.email());
        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }

}
