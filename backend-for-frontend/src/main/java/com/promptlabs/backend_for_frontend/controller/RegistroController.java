package com.promptlabs.backend_for_frontend.controller;


import com.promptlabs.backend_for_frontend.dto.SuperRegistroDTO;
import com.promptlabs.backend_for_frontend.service.RegistroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/registro")
@RequiredArgsConstructor
@Tag(name = "Orquestación de Registro", description = "Endpoints para el registro completo de usuarios y perfiles")
@CrossOrigin(origins = "*")
public class RegistroController {
   private final RegistroService registroService;

    @Operation(summary = "Registro completo de usuario", description = "Orquesta el registro en autenticación y la creación del perfil en el servicio de usuarios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro orquestado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de registro")
    })
    @PostMapping("/registrar-usuario")
    public ResponseEntity<Map<String, Object>> registrarUsuario(
            @RequestHeader(value = "X-Device-ID", required = false) String deviceId,
            @RequestBody SuperRegistroDTO formularioCompleto) {

        return ResponseEntity.ok(registroService.registrar(formularioCompleto, deviceId));
    }
}
