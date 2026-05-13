package com.promptlabs.usuarios_perfiles.service.strategy;

import com.promptlabs.usuarios_perfiles.entity.User;

public interface ProfileCreationStrategy {

    String getRole();

    void createEmptyProfile(User user);

}
