package com.promptlabs.backend_for_frontend.controller;

import com.promptlabs.backend_for_frontend.dto.UserSummaryResponse;
import com.promptlabs.backend_for_frontend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Usuarios", description = "Endpoints para la gestión y consulta de perfiles de usuario.")
public class UsersController {
    private final UserService userService;

    @Operation(summary = "Listar usuarios", description = "Devuelve una lista resumida de todos los usuarios registrados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<UserSummaryResponse>> listUsers() {
        return ResponseEntity.ok(
                userService.listUsers()
        );
    }
}
