package com.promptlabs.usuarios_perfiles.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "teacher_profiles")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherProfile {

    @Id
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 150)
    private String biography;

    @Column(length = 70)
    private String office;

    @Column(length = 70)
    private String availabilityHours;
    private String academicGrade;
}
