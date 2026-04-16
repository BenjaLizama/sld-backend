package com.promptlabs.autenticacion_seguridad.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StandarErrorResponse {

    private Integer status;                      // Ejemplo: 404.
    private String code;                         // Código interno (ej: AUTH_001)
    private String error;                        // Ejemplo: "NotFound".
    private String message;                      // Aquí va el mensaje que se muestra en el frontend.
    private String developerMessage;             // Aquí va el mensaje que pone el desarrollador.
    private String path;                         // La ruta que falló (ej: /auth/login).
    private Long timestamp;                      // La hora/minuto/segundo en que ocurrió el error.
    private Map<String, String> validationError; // Para errores de formularios.

}
