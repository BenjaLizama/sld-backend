package com.promptlabs.usuarios_perfiles.controller;

import com.promptlabs.usuarios_perfiles.dto.UserProfileCompletionRequest;
import com.promptlabs.usuarios_perfiles.dto.UserResponse;
import com.promptlabs.usuarios_perfiles.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "operaciones relacionadas con el perfil base del usuario")
public class UserProfileController {

    private final UserService userService;

    @Operation(summary = "Completar el perfil base",
            description = "Permite a un usuario completar su perfil base")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil completado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")})
    @PutMapping("/{userId}/complete-profile")
    public ResponseEntity<UserResponse> completeProfile(
            @Parameter(description = "ID único del usuario (UUID)") @PathVariable UUID userId,
            @Valid @RequestBody UserProfileCompletionRequest request
    ) {
        userService.completarPerfilBase(userId, request);
        UserResponse response = new UserResponse(
                request.firstName(),
                request.rut(),
                "Perfil base actualizado con éxito"
        );

        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Completar el perfil especifico de un usuario",
            description = "Permite a un usuario completar su perfil especializado ej (Profesor,Estidiante,Apoderado)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil completado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")})
    @PutMapping("/{userId}/profile-update")
    public ResponseEntity<String> updateProfile(
            @RequestHeader("Authorization") String token,
            @Parameter(description = "ID único del usuario (UUID)") @PathVariable UUID userId,
            @RequestBody Map<String, Object> body
    ) {
        System.out.println("llamada correcta");

        userService.actualizarPerfilEspecifico(userId, token, body);

        return ResponseEntity.ok("Procesado correctamente");
    }
}