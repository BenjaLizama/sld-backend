package com.promptlabs.autenticacion_seguridad.dto;

// TODO: UTILIZAR VALIDATIONS AQUI
public record SessionRequest(
        String deviceId,
        String deviceName,
        String ipAddress,
        String ua
) {}
