package com.promptLabs.ms_notas.dto;

public record ErrorMessageDto(
        String message,
        int status
) {
}
