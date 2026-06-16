package com.promptlabs.usuarios_perfiles.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI usuariosPerfilesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Usuarios y Perfiles")
                        .version("1.0")
                        .description("Servicio encargado de la gestión de datos maestros de usuarios, perfiles de estudiantes, profesores y apoderados.")
                        .contact(new Contact()
                                .name("PromptLabs Support")
                                .email("soporte@promptlabs.com")));
    }
}
