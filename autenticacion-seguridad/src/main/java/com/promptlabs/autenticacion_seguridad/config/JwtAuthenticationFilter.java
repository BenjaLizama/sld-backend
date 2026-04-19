package com.promptlabs.autenticacion_seguridad.config;

import com.promptlabs.autenticacion_seguridad.service.impl.CustomUserDetailsService;
import com.promptlabs.autenticacion_seguridad.service.impl.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Si no tenemos header authorization o no empieza con bearer, seguimos con la cadena de filtros.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extraemos el token (quitando "bearer")
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        // 3. Si el email existe y el usuario NO está autenticado en el contexto actual.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 4. Validar si el token es correcto para este usuario.
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // 1. Extraer claims del token directamente.
                Claims claims = jwtService.extractAllClaims(jwt);

                // 2. Obtenemos las listas que guardamos al generar el token.
                List<String> roles = claims.get("roles", List.class);
                List<String> privileges = claims.get("privileges", List.class);

                // 3. Convertimos a GrantedAuthority de Spring Security.
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                if (roles != null) roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                if (privileges != null) privileges.forEach(privilege -> authorities.add(new SimpleGrantedAuthority(privilege)));

                // 4. Creamos el token de autenticación con las autoridades del JWT.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
