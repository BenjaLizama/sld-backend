package com.promptlabs.autenticacion_seguridad.service;

import org.springframework.stereotype.Service;

@Service
public interface ITokenHashService {
    String hash(String token);
}
