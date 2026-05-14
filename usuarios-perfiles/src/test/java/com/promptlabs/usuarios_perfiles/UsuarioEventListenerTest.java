package com.promptlabs.usuarios_perfiles;

import com.promptlabs.usuarios_perfiles.dto.UserCreatedEvent;
import com.promptlabs.usuarios_perfiles.messaging.UserEventListener;
import com.promptlabs.usuarios_perfiles.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UsuarioEventListenerTest {

    @Autowired
    private UserEventListener listener; // Tu clase que escucha Rabbit

    @Autowired
    private UserRepository repository;

    @Test
    void cuandoLlegaEventoRegistro_debeCrearUsuarioEnBaseDeDatos() {
        // 1. Simular el evento que vendría de Seguridad
        UserCreatedEvent evento = new UserCreatedEvent(UUID.randomUUID(), "test@mail.com", "ROLE_TEACHER");

        // 2. Ejecutar el listener manualmente
        listener.onUserCreated(evento);

        // 3. Verificar que se guardó
        assertTrue(repository.existsById(evento.userId()));
    }
}
