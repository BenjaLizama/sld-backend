package com.promptlabs.autenticacion_seguridad.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI autenticacionSeguridadOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Autenticación y Seguridad")
                        .version("1.0")
                        .description("Servicio encargado de la gestión de credenciales, roles, privilegios y emisión de tokens JWT.")
                        .contact(new Contact()
                                .name("PromptLabs Support")
                                .email("soporte@promptlabs.com")));
    }
}
