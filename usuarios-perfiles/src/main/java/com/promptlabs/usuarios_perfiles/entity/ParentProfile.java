package com.promptlabs.usuarios_perfiles.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Table(name = "parents_profiles")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParentProfile {

    @Id
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    //considerar cambiar a un diccionario
    private String educationLevel;


    private Boolean isSupporter;

    //AGREGAR PARENTESCO

}
