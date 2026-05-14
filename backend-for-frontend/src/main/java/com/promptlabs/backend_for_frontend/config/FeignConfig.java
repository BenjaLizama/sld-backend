package com.promptlabs.backend_for_frontend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Configuration // <--- Importante para que Spring la lea
public class FeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    // Puedes poner la clase interna aquí mismo o en un archivo aparte
    public static class CustomErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder defaultDecoder = new Default();

        @Override
        public Exception decode(String methodKey, Response response) {
            // Si no hay cuerpo en la respuesta, usamos el decodificador por defecto
            if (response.body() == null) {
                return defaultDecoder.decode(methodKey, response);
            }

            try (InputStream bodyIs = response.body().asInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> errorDetails = mapper.readValue(bodyIs, Map.class);
                String message = (String) errorDetails.getOrDefault("message", "Error desconocido");

                return switch (response.status()) {
                    case 400 -> new BadRequestException(message);
                    case 404 -> new EntityNotFoundException(message);
                    default -> defaultDecoder.decode(methodKey, response);
                };
            } catch (IOException e) {
                return defaultDecoder.decode(methodKey, response);
            }
        }
    }
}
