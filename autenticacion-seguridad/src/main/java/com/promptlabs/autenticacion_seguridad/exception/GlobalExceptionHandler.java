package com.promptlabs.autenticacion_seguridad.exception;

import com.promptlabs.autenticacion_seguridad.dto.StandarErrorResponse;
import com.promptlabs.autenticacion_seguridad.mapper.ErrorMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorMapper errorMapper;

    /**
     * 1. Manejo de errores de validación DTO (@Valid).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandarErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("Error de validación en la ruta {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMapper.toValidationResponse(ex, request.getRequestURI()));
    }

    /**
     * 2. Errores de reglas de negocio (ej: El correo ya existe).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandarErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Error de negocio en la ruta {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMapper.toBusinessResponse(ex, request.getRequestURI()));
    }

    /**
     * 3. Errores de credenciales (correo o contraseña incorrectos).
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandarErrorResponse> handleBadCredentialException(BadCredentialsException ex, HttpServletRequest request) {
        log.warn("Intento de login fallido en la ruta {}", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(errorMapper.toAuthResponse(ex, request.getRequestURI()));
    }

    /**
     * 4. JSON Malformado enviado por el cliente.
     */
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<StandarErrorResponse> handleHttpMessageNotReadable(Exception ex, HttpServletRequest request) {
        log.warn("Petición malformada en la ruta {}: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMapper.toMalformedRequestResponse(ex, request.getRequestURI()));
    }

    /**
     * 5. Acceso Denegado (403 Forbidden) - El usuario no tiene el rol necesario.
     */
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<StandarErrorResponse> handleAccessDeniedException(Exception ex, HttpServletRequest request) {
        log.warn("Acceso denegado en la ruta {}: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(errorMapper.toAccessDeniedResponse(ex, request.getRequestURI()));
    }

    /**
     * 6. Método HTTP no soportado (ej. usar GET en lugar de POST).
     */
    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<StandarErrorResponse> handleMethodNotSupportedException(Exception ex, HttpServletRequest request) {
        log.warn("Método HTTP no soportado en la ruta {}: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(errorMapper.toMethodNotSupportedResponse(ex, request.getRequestURI()));
    }

    /**
     * 0. Error genérico de servidor.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandarErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
        log.error("Error crítico inesperado en la ruta {}: ", request.getRequestURI(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorMapper.toSystemErrorResponse(ex, request.getRequestURI()));
    }

}
