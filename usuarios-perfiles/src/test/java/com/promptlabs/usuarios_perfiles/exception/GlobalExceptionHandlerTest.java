package com.promptlabs.usuarios_perfiles.exception;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }


    @RestController
    static class TestController {
        @GetMapping("/test/not-found")
        public void throwNotFound() {
            throw new EntityNotFoundException("Elemento no encontrado");
        }

        @GetMapping("/test/relationship")
        public void throwRelationship() {
            throw new RelationshipException("Conflicto de relación");
        }

        @GetMapping("/test/runtime")
        public void throwRuntime() {
            throw new RuntimeException("Error de tiempo de ejecución");
        }

        @GetMapping("/test/general")
        public void throwGeneral() throws Exception {
            throw new Exception("Error genérico");
        }
    }

    @Test
    @DisplayName("Debe retornar 404 cuando se lanza EntityNotFoundException")
    void handleNotFound_Returns404() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Elemento no encontrado"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    @DisplayName("Debe retornar 409 cuando se lanza RelationshipException")
    void handleRelationship_Returns409() throws Exception {
        mockMvc.perform(get("/test/relationship"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Conflicto de relación"))
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("Debe retornar 400 cuando se lanza RuntimeException")
    void handleRuntime_Returns400() throws Exception {
        mockMvc.perform(get("/test/runtime"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error de tiempo de ejecución"));
    }

    @Test
    @DisplayName("Debe retornar 500 cuando se lanza una Exception genérica")
    void handleGeneral_Returns500() throws Exception {
        mockMvc.perform(get("/test/general"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Error interno del servidor"));
    }
}
