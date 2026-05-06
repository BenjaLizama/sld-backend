package com.promptlabs.usuarios_perfiles.service.strategy;

import com.promptlabs.usuarios_perfiles.entity.TeacherProfile;
import com.promptlabs.usuarios_perfiles.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TeacherProfileStrategy implements ProfileCreationStrategy {

    @Override
    public String getRole() {
        return "TEACHER";
    }

    @Override
    public void createEmptyProfile(User user) {
        TeacherProfile profile = TeacherProfile.builder().user(user).build();

        user.setTeacherProfile(profile);


    }
}
