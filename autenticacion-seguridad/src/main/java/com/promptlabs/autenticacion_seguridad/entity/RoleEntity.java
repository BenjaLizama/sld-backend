package com.promptlabs.autenticacion_seguridad.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Table(name = "ROLE")
public class RoleEntity extends BaseEntity {

    @Column(nullable = false)
    private String roleName;

    @Column(nullable = false)
    private String roleDescription;

}
