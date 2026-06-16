package com.promptlabs.backend_for_frontend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI backendForFrontendOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gateway (BFF)")
                        .version("1.0")
                        .description("Servicio encargado de orquestar las peticiones desde el frontend hacia los distintos microservicios del ecosistema.")
                        .contact(new Contact()
                                .name("PromptLabs Support")
                                .email("soporte@promptlabs.com")));
    }
}
