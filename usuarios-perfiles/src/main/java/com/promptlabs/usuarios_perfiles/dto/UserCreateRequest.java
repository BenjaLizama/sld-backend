package com.promptlabs.usuarios_perfiles.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UserCreateRequest(

        //DATOS QUE VIENEN DESDE AUTH
        UUID id,
        String email,
        String role,

        //DATOS PERSONALES PARA EL PERFIL
        String rut,
        String firstName,
        String middleName,
        String lastName,
        String secondLastName,
        String phoneNumber,
        String address,
        LocalDate birthday,
        String nationality,
        Long genderId
) {
}
