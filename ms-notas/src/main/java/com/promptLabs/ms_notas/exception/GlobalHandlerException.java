package com.promptLabs.ms_notas.exception;

import com.promptLabs.ms_notas.dto.ErrorMessageDto;
import com.promptLabs.ms_notas.dto.ValidationErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandlerException {
    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ErrorMessageDto> handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorMessageDto errorDto = new ErrorMessageDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDto> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        });

        // Aquí usamos tu nuevo DTO para mantener la consistencia
        ValidationErrorDto errorDto = new ValidationErrorDto(
                "Error en la validación de los datos enviados",
                HttpStatus.BAD_REQUEST.value(),
                fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }
}
