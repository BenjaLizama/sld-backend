package com.promptlabs.usuarios_perfiles.mapper;

import com.promptlabs.usuarios_perfiles.dto.UserSummaryDTO;
import com.promptlabs.usuarios_perfiles.entity.User;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public UserSummaryDTO toSummary(User user) {
        String fullName = String.join(" ",
                user.getFirstName(),
                user.getMiddleName() == null ? "" : user.getMiddleName(),
                user.getLastName(),
                user.getSecondLastName() == null ? "" : user.getSecondLastName()
        ).trim().replaceAll("\\s+", " ");


        return new UserSummaryDTO(
                fullName,
                user.getEmail(),
                user.getAddress(),
                user.getCreationDate(),
                user.getNationality(),
                user.getGender().getGender(),
                user.getRut(),
                user.getBirthday(),
                user.getPhoneNumber())
                ;
    }
}
