package com.promptLabs.ms_notas.dto;

import java.util.Map;

public record ValidationErrorDto(
        String message,
        int status,
        Map<String, String> errors
) {}