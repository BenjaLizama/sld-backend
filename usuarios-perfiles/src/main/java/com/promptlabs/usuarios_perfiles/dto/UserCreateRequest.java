package com.promptlabs.usuarios_perfiles.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UserCreateRequest(

        //Datos del ms auth
        String email,
        UUID id,
        String rol,

        //Datos personales
        String rut,
        String firstName,
        String middleName,
        String lastName,
        String secondLastName,
        String phoneNumber,
        String address,
        LocalDate birthday,
        String nationality

) {
}
