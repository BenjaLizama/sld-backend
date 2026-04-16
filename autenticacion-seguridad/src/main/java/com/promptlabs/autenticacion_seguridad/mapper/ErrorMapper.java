package com.promptlabs.autenticacion_seguridad.mapper;

import com.promptlabs.autenticacion_seguridad.dto.StandarErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

@Component
public class ErrorMapper {

    /**
     * BASE PARA EL RESTO DE MAPPERS
     */
    private StandarErrorResponse.StandarErrorResponseBuilder buildBaseError(HttpStatus status, String path) {
        return StandarErrorResponse.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .path(path)
                .timestamp(System.currentTimeMillis());
    }

    // ==========================================================
    // MÉTODOS PÚBLICOS
    // ==========================================================

    public StandarErrorResponse toValidationResponse(MethodArgumentNotValidException ex, String path) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return buildBaseError(HttpStatus.BAD_REQUEST, path)
                .code("ERR_VALIDATION_001")
                .message("Por favor, verifica los datos ingresados.")
                .developerMessage("La validación del DTO falló en uno o más campos.")
                .validationError(errors)
                .build();
    }

    public StandarErrorResponse toBusinessResponse(IllegalArgumentException ex, String path) {
        return buildBaseError(HttpStatus.BAD_REQUEST, path)
                .code("ERR_BUSINESS_001")
                .message(ex.getMessage())
                .developerMessage("Se recibió un argumento inválido que viola las reglas de negocio.")
                .build();
    }

    public StandarErrorResponse toAuthResponse(Exception ex, String path) {
        return buildBaseError(HttpStatus.UNAUTHORIZED, path)
                .code("AUTH_001")
                .message("Correo o contraseña incorrectos.")
                .developerMessage(ex.getMessage())
                .build();
    }

    public StandarErrorResponse toSystemErrorResponse(Exception ex, String path) {
        return buildBaseError(HttpStatus.INTERNAL_SERVER_ERROR, path)
                .code("SYS_500")
                .message("Ocurrió un error inesperado en el servidor. Por favor, intenta más tarde.")
                .developerMessage(ex.toString())
                .build();
    }

    public StandarErrorResponse toMalformedRequestResponse(Exception ex, String path) {
        return buildBaseError(HttpStatus.BAD_REQUEST, path)
                .code("ERR_MALFORMED_001")
                .message("El formato de la petición es inválido. Revisa la estructura de los datos.")
                .developerMessage(ex.getMessage())
                .build();
    }

    public StandarErrorResponse toAccessDeniedResponse(Exception ex, String path) {
        return buildBaseError(HttpStatus.FORBIDDEN, path)
                .code("ERR_FORBIDDEN_001")
                .message("No tienes los permisos necesarios para realizar esta acción.")
                .developerMessage(ex.getMessage())
                .build();
    }

    public StandarErrorResponse toMethodNotSupportedResponse(Exception ex, String path) {
        return buildBaseError(HttpStatus.METHOD_NOT_ALLOWED, path)
                .code("ERR_METHOD_001")
                .message("El método HTTP utilizado no está soportado para esta ruta.")
                .developerMessage(ex.getMessage())
                .build();
    }

}
