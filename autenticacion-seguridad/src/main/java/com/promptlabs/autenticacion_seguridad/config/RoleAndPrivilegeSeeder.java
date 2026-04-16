package com.promptlabs.autenticacion_seguridad.config;

import com.promptlabs.autenticacion_seguridad.entity.PrivilegeEntity;
import com.promptlabs.autenticacion_seguridad.entity.RoleEntity;
import com.promptlabs.autenticacion_seguridad.repository.PrivilegeRepository;
import com.promptlabs.autenticacion_seguridad.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleAndPrivilegeSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            log.info("--- Iniciando Seeder de Seguridad ---");

            // 1. Crear Privilegios
            PrivilegeEntity readPriv = createPrivilegeIfNotFound("READ_PRIVILEGE", "Permite leer información básica");
            PrivilegeEntity writePriv = createPrivilegeIfNotFound("WRITE_PRIVILEGE", "Permite crear y editar información");
            PrivilegeEntity deletePriv = createPrivilegeIfNotFound("DELETE_PRIVILEGE", "Permite eliminar registros");

            // 2. Crear Rol ADMIN (Con todos los privilegios)
            createRoleIfNotFound("ROLE_ADMIN", "Administrador total del sistema",
                    Set.of(readPriv, writePriv, deletePriv));

            // 3. Crear Rol USER
            createRoleIfNotFound("ROLE_USER", "Usuario estándar de la aplicación",
                    Set.of(readPriv));

            log.info("--- Seeder de Seguridad finalizado con éxito ---");
        }
    }

    private PrivilegeEntity createPrivilegeIfNotFound(String name, String description) {
        return privilegeRepository.findByName(name)
                .orElseGet(() -> {
                    PrivilegeEntity privilege = new PrivilegeEntity();
                    privilege.setName(name);
                    privilege.setDescription(description);
                    privilege.setIsValid(true);
                    return privilegeRepository.save(privilege);
                });
    }

    private void createRoleIfNotFound(String name, String description, Set<PrivilegeEntity> privileges) {
        roleRepository.findByRoleName(name).ifPresentOrElse(
                role -> log.info("El rol {} ya existe, omitiendo...", name),
                () -> {
                    RoleEntity role = new RoleEntity();
                    role.setRoleName(name);
                    role.setRoleDescription(description);
                    role.setPrivileges(privileges);
                    role.setIsValid(true);
                    roleRepository.save(role);
                    log.info("Rol {} creado y asociado con sus privilegios.", name);
                }
        );
    }

}
