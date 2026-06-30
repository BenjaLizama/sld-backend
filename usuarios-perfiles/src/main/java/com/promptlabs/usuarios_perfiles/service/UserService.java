package com.promptlabs.usuarios_perfiles.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptlabs.usuarios_perfiles.dto.*;
import com.promptlabs.usuarios_perfiles.entity.Gender;
import com.promptlabs.usuarios_perfiles.entity.User;
import com.promptlabs.usuarios_perfiles.mapper.UserMapper;
import com.promptlabs.usuarios_perfiles.repository.GenderRepository;
import com.promptlabs.usuarios_perfiles.repository.UserRepository;
import com.promptlabs.usuarios_perfiles.service.strategy.ProfileCreationStrategy;
import com.promptlabs.usuarios_perfiles.utils.TokenUtils;
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
    private final GenderRepository genderRepository;

    private final TeacherProfileService teacherProfileService;
    private final StudentProfileService studentProfileService;
    private final ParentProfileService parentProfileService;
    private final ObjectMapper objectMapper;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, List<ProfileCreationStrategy> strategyList, GenderRepository genderRepository, TeacherProfileService teacherProfileService, StudentProfileService studentProfileService, ParentProfileService parentProfileService, ObjectMapper objectMapper, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        s -> s.getRole().toUpperCase(),
                        s -> s
                ));
        this.genderRepository = genderRepository;
        this.teacherProfileService = teacherProfileService;
        this.studentProfileService = studentProfileService;
        this.parentProfileService = parentProfileService;
        this.objectMapper = objectMapper;
        this.userMapper = userMapper;
    }

    @Transactional
    public void crearUserCascaron(AuthRegistrationRequest request) {
        User user = User.builder()
                .id(request.id())
                .email(request.email())
                .creationDate(Instant.now())
                .build();
        String roleKey = request.role().toUpperCase();
        ProfileCreationStrategy strategy = strategies.get(roleKey);

        if (roleKey.equals("ROLE_USER")) {
            System.out.println("ℹ️ Rol " + roleKey + " no requiere perfil específico. Creando solo usuario base.");
        } else if (strategy != null) {
            strategy.createEmptyProfile(user);
            System.out.println("estrateguia para rol" + roleKey);
        } else {
            throw new RuntimeException("El rol" + roleKey + " no tiene estrategia");
        }
        userRepository.save(user);
    }

    @Transactional
    public void completarPerfilBase(UUID userId, UserProfileCompletionRequest request) {

        Gender gender = genderRepository.findById(request.genderId())
                .orElseThrow(() -> new RuntimeException("Género no encontrado con ID: " + request.genderId()));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));

        user.setRut(request.rut());
        user.setFirstName(request.firstName());
        user.setMiddleName(request.middleName());
        user.setLastName(request.lastName());
        user.setSecondLastName(request.secondLastName());
        user.setGender(gender);
        user.setPhoneNumber(request.phoneNumber());
        user.setAddress(request.address());
        user.setBirthday(request.birthday());
        user.setNationality(request.nationality());


        userRepository.save(user);
    }

    @Transactional
    public void actualizarPerfilEspecifico(UUID userId, String token, Map<String, Object> datos) {

        String roleFromToken = TokenUtils.getRoleFromToken(token);

        switch (roleFromToken) {
            case "ROLE_TEACHER":
                TeacherUpdateRequest teacherDTO = objectMapper.convertValue(datos, TeacherUpdateRequest.class);
                teacherProfileService.updateTeacherInfo(userId, teacherDTO);
                break;

            case "ROLE_STUDENT":
                StudentInformationUpdateRequest studentDTO = objectMapper.convertValue(datos, StudentInformationUpdateRequest.class);
                studentProfileService.updateMedicalInfo(userId, studentDTO);
                break;

            case "ROLE_PARENT":
                ParentInformationUpdateRequest parentDTO = objectMapper.convertValue(datos, ParentInformationUpdateRequest.class);
                parentProfileService.updateParentInfo(userId, parentDTO);
                System.out.println("se actualizo el perifl" + userId + parentDTO);
                break;
            case "ROLE_USER":
                throw new RuntimeException("el usuario se guardo sin perfil asignado");

            default:
                throw new RuntimeException("No hay lógica de actualización para el rol: " + roleFromToken);
        }
    }


    public boolean existeUsuario(UUID id) {
        return userRepository.existsById(id);
    }

    public List<UserSummaryDTO> listUsers() {
        return userRepository
                .findAll()
                .stream()
                .filter(user -> user.getFirstName() != null)
                .map(userMapper::toSummary)
                .toList();
    }


}
