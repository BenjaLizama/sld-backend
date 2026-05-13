package com.promptlabs.usuarios_perfiles.messaging;

import com.promptlabs.usuarios_perfiles.config.RabbitMQConfig;
import com.promptlabs.usuarios_perfiles.dto.AuthRegistrationRequest;
import com.promptlabs.usuarios_perfiles.dto.UserCreatedEvent;
import com.promptlabs.usuarios_perfiles.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final UserService userService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE,concurrency = "1")
    public void onUserCreated(UserCreatedEvent event) {



        AuthRegistrationRequest request = new AuthRegistrationRequest(
                event.email(),
                event.userId(),
                event.role()
        );

        userService.crearUserCascaron(request);
    }
}