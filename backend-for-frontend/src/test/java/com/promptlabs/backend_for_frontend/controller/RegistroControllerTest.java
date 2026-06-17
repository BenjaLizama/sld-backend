package com.promptlabs.backend_for_frontend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptlabs.backend_for_frontend.dto.SuperRegistroDTO;
import com.promptlabs.backend_for_frontend.service.RegistroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RegistroControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RegistroService registroService;

    @InjectMocks
    private RegistroController registroController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(registroController).build();
    }

    @Test
    void registrarUsuario_ShouldReturnOk() throws Exception {
        SuperRegistroDTO request = mock(SuperRegistroDTO.class);
        Map<String, Object> response = Map.of("perfilCompletado", true, "rolAsignado", "ROLE_USER");

        when(registroService.registrar(any(SuperRegistroDTO.class), eq("dev-123"))).thenReturn(response);

        mockMvc.perform(post("/api/v1/registro/registrar-usuario")
                        .header("X-Device-ID", "dev-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.perfilCompletado").value(true))
                .andExpect(jsonPath("$.rolAsignado").value("ROLE_USER"));
    }
}
