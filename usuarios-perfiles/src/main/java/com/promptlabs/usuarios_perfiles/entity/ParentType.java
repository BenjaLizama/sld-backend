package com.promptlabs.usuarios_perfiles.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "parent_type")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String relationship;
}
