package com.promptlabs.usuarios_perfiles.entity;

import com.promptlabs.usuarios_perfiles.config.AesEncryptor;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Table(name = "usuarios")
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private UUID id;

    @Column(unique = true)
    private String rut;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "gender_id")
    private Gender gender;

    //agregamos excluciones para evitar bucles que saturen la memoria
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private StudentProfile studentProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TeacherProfile teacherProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ParentProfile parentProfile;

    @Convert(converter = AesEncryptor.class)
    private String firstName;

    @Convert(converter = AesEncryptor.class)
    private String middleName;

    @Convert(converter = AesEncryptor.class)
    private String lastName;

    @Convert(converter = AesEncryptor.class)
    private String secondLastName;

    @Convert(converter = AesEncryptor.class)
    private String phoneNumber;

    @Convert(converter = AesEncryptor.class)
    private String address;

    private LocalDate birthday;

    @Convert(converter = AesEncryptor.class)
    private String nationality;

    private Instant creationDate;

}
