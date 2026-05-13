package com.promptlabs.usuarios_perfiles.service.strategy;

import com.promptlabs.usuarios_perfiles.entity.ParentProfile;
import com.promptlabs.usuarios_perfiles.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ParentProfileStrategy implements ProfileCreationStrategy {

    @Override
    public String getRole() {
        return "ROLE_PARENT";
    }

    @Override
    public void createEmptyProfile(User user) {
        ParentProfile profile = ParentProfile.builder().user(user).build();

        user.setParentProfile(profile);
    }
}
