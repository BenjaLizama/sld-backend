package com.promptlabs.backend_for_frontend.controller;


import com.promptlabs.backend_for_frontend.dto.SuperRegistroDTO;
import com.promptlabs.backend_for_frontend.service.RegistroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/registro")
@RequiredArgsConstructor
public class RgistroController {
   private final RegistroService registroService;

    @PostMapping("/registrar-usuario")
    public ResponseEntity<Map<String, Object>> registrarUsuario(
            @RequestHeader(value = "X-Device-ID", required = false) String deviceId,
            @RequestBody SuperRegistroDTO formularioCompleto) {

        return ResponseEntity.ok(registroService.registrar(formularioCompleto, deviceId));
    }
}
