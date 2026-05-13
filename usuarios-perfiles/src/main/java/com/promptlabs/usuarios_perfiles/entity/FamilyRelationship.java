package com.promptlabs.usuarios_perfiles.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "family_relationship")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FamilyRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    private ParentProfile parentProfile;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentProfile studentProfile;

    @ManyToOne
    @JoinColumn(name = "relationship_id", nullable = false)
    private ParentType parentType;

}
