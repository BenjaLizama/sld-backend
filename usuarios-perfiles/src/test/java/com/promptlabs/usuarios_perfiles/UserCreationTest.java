package com.promptlabs.usuarios_perfiles;

import com.promptlabs.usuarios_perfiles.dto.UserProfileCompletionRequest;
import com.promptlabs.usuarios_perfiles.entity.Gender;
import com.promptlabs.usuarios_perfiles.entity.User;
import com.promptlabs.usuarios_perfiles.repository.GenderRepository;
import com.promptlabs.usuarios_perfiles.repository.UserRepository;
import com.promptlabs.usuarios_perfiles.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional // Esto hace que cada test limpie sus datos al terminar (Rollback)
public class UserCreationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenderRepository genderRepository;

    @Test
    void cuandoCompletarPerfilBase_debeGuardarDatosCorrectamente() {
        // 1. Preparar datos: Crear Género y Usuario inicial
        Gender gender = genderRepository.save(new Gender(1, "MASCULINO")); // Ajusta según tu entidad Gender

        UUID userId = UUID.randomUUID();
        userRepository.save(User.builder()
                .id(userId)
                .email("test@mail.com")
                .build());

        // 2. Crear el DTO manualmente (Simulando lo que llegaría del BFF)
        UserProfileCompletionRequest request = new UserProfileCompletionRequest(
                "12345678-9",
                "Lucci",
                null,
                "Developer",
                null,
                1, // genderId
                "+56912345678",
                "Calle Falsa 123",
                LocalDate.of(1995, 5, 13),
                "Chilena"
        );

        // 3. Llamar directamente al servicio (Sin pasar por el Controller)
        userService.completarPerfilBase(userId, request);

        // 4. Verificar en la DB
        User userGuardado = userRepository.findById(userId).orElseThrow();

        assertEquals("12345678-9", userGuardado.getRut());
        assertEquals("Lucci", userGuardado.getFirstName());
        assertEquals("MASCULINO", userGuardado.getGender().getName());
    }
}