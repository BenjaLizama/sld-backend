package com.promptlabs.usuarios_perfiles.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/v1/internal/**").permitAll()
                        .requestMatchers("/api/v1/users/**").permitAll()
                        .requestMatchers("/api/v1/students/**").permitAll()
                        .requestMatchers("/api/v1/parents/**").permitAll()
                        .requestMatchers("/api/v1/families/**").permitAll()
                        .requestMatchers("/api/v1/teachers/**").permitAll()
                        // 👇 AGREGA ESTA LÍNEA (EL SALVAVIDAS)
                        .requestMatchers("/error").permitAll()

                        .anyRequest().authenticated()
                )
                // 4. Permitimos los iframes para que la consola H2 se pueda renderizar
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return httpSecurity.build();
    }
}