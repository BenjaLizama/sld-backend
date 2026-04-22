package com.promptlabs.usuarios_perfiles.entity;

import com.promptlabs.usuarios_perfiles.AesEncryptor;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Table(name = "usuarios")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
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

    @NonNull
    @Convert(converter = AesEncryptor.class)
    private String firstName;

    @Convert(converter = AesEncryptor.class)
    private String middleName;

    @NonNull
    @Convert(converter = AesEncryptor.class)
    private String lastName;

    @Convert(converter = AesEncryptor.class)
    private String secondLastName;

    @NonNull
    @Convert(converter = AesEncryptor.class)
    private String phoneNumber;

    @NonNull
    @Convert(converter = AesEncryptor.class)
    private String address;

    @NonNull
    @Convert(converter = AesEncryptor.class)
    private LocalDate birthday;

    @NonNull
    @Convert(converter = AesEncryptor.class)
    private String nationality;

    @NonNull
    @Convert(converter = AesEncryptor.class)
    private Instant creationDate;

}
