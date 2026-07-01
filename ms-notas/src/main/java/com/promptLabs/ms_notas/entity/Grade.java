package com.promptLabs.ms_notas.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "grade")
@Getter
@Setter
@NoArgsConstructor
public class Grade {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "grade_value", nullable = false)
    private Double value;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private UUID teacherId;

    @Column(nullable = false)
    private String name;
}
