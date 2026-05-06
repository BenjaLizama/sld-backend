package com.promptlabs.usuarios_perfiles.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Table(name = "student_profiles")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentProfile {

    @Id
    private UUID id;

    @Column()
    private String medicConditions;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

}
