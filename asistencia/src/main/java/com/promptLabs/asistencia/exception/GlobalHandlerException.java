package com.promptLabs.asistencia.exception;


import com.promptLabs.asistencia.dto.ErrorMessageDto;
import com.promptLabs.asistencia.dto.ValidationErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler({AttendanceConflict.class})
    public ResponseEntity<ErrorMessageDto> handleAttendanceConflict(AttendanceConflict e) {
        ErrorMessageDto errorDto = new ErrorMessageDto(e.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
    }

    @ExceptionHandler({ResourceNotFound.class})
    public ResponseEntity<ErrorMessageDto> handleResourceNotFound(ResourceNotFound e) {
        ErrorMessageDto errorDto = new ErrorMessageDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ValidationErrorDto> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {

        Map<String, String> fieldErrors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach((error) -> {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        });
        ValidationErrorDto errorDto = new ValidationErrorDto("Error en la validacion", HttpStatus.BAD_REQUEST.value(), fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

}
