package com.promptlabs.usuarios_perfiles.service.strategy;

import com.promptlabs.usuarios_perfiles.entity.StudentProfile;
import com.promptlabs.usuarios_perfiles.entity.User;
import org.springframework.stereotype.Component;

@Component
public class StudentProfileStrategy implements ProfileCreationStrategy {


    @Override
    public String getRole() {
        return "ROLE_STUDENT";
    }

    @Override
    public void createEmptyProfile(User user){
        StudentProfile profile = StudentProfile.builder().user(user).build();

        user.setStudentProfile(profile);


    }
}
