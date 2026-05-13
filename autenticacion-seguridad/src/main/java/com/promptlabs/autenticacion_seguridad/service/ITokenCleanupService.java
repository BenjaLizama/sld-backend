package com.promptlabs.autenticacion_seguridad.service;

public interface ITokenCleanupService {
    void cleanExpiredTokens();
}
