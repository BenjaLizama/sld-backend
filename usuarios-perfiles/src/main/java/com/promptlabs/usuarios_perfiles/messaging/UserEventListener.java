package com.promptlabs.usuarios_perfiles.messaging;

import com.promptlabs.usuarios_perfiles.config.RabbitMQConfig;
import com.promptlabs.usuarios_perfiles.dto.AuthRegistrationRequest;
import com.promptlabs.usuarios_perfiles.dto.UserCreatedEvent;
import com.promptlabs.usuarios_perfiles.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final UserService userService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE, concurrency = "1")
    public void onUserCreated(UserCreatedEvent event) {
        // 1. Log inicial para saber que llegó el mensaje
        System.out.println("📩 Recibiendo evento para usuario: " + event.userId());

        // 2. Validación Defensiva: ¿Ya existe?
        if (userService.existeUsuario(event.userId())) {
            System.out.println("⚠️ El usuario " + event.userId() + " ya existe. Ignorando mensaje para evitar bucle.");
            return; // Salimos sin lanzar excepción, Rabbit marcará el mensaje como procesado (ACK)
        }

        try {
            AuthRegistrationRequest request = new AuthRegistrationRequest(
                    event.email(),
                    event.userId(),
                    event.role()
            );
            userService.crearUserCascaron(request);
            System.out.println("✅ Usuario cascarón creado exitosamente.");

        } catch (DataIntegrityViolationException e) {
            System.err.println("❌ Error de integridad: " + e.getMessage());
            // Esto le dice a Rabbit: "No me lo mandes más, está mal o duplicado"
            throw new AmqpRejectAndDontRequeueException("Dato duplicado, descartando mensaje", e);
        } catch (Exception e) {
            System.err.println("❌ Error inesperado: " + e.getMessage());
            // Si es un error temporal (ej. DB caída), podrías querer que se reencole.
            // Pero si quieres evitar el bucle a toda costa, usa la misma excepción de arriba.
            throw new AmqpRejectAndDontRequeueException("Error fatal, descartando mensaje", e);
        }
    }

}