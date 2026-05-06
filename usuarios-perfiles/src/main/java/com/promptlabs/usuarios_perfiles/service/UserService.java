package com.promptlabs.usuarios_perfiles.service;

import com.promptlabs.usuarios_perfiles.dto.AuthRegistrationRequest;
import com.promptlabs.usuarios_perfiles.dto.UserProfileCompletionRequest;
import com.promptlabs.usuarios_perfiles.entity.User;
import com.promptlabs.usuarios_perfiles.repository.UserRepository;
import com.promptlabs.usuarios_perfiles.service.strategy.ProfileCreationStrategy;
import org.springframework.stereotype.Service;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Map<String, ProfileCreationStrategy> strategies;

    public UserService(UserRepository userRepository, List<ProfileCreationStrategy> strategyList) {
        this.userRepository = userRepository;
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        s -> s.getRole().toUpperCase(),
                        s -> s
                ));
    }

    @Transactional
    public void crearUserCascaron(AuthRegistrationRequest request) {
        User user = User.builder()
                .id(request.id())
                .email(request.email())
                .creationDate(Instant.now())
                .build();
        String roleKey =request.role().toUpperCase();
        ProfileCreationStrategy strategy = strategies.get(roleKey);

        if (strategy!=null) {
            strategy.createEmptyProfile(user);
            System.out.println("estrateguia para rol"+ roleKey);
        }else{
            throw new RuntimeException("El rol" + roleKey + " no tiene estrategia");
        }
    userRepository.save(user);
    }

    @Transactional
    public void completarPerfilBase(UUID userId, UserProfileCompletionRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));

        user.setRut(request.rut());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setSecondLastName(request.secondLastName());
        user.setBirthday(request.birthday());
        user.setPhoneNumber(request.phoneNumber());
        user.setAddress(request.address());

        userRepository.save(user);

        System.out.println("✅ Perfil base completado para el usuario ID: " + userId);
    }
}
