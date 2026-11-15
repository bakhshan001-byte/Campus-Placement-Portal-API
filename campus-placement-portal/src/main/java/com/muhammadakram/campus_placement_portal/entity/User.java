package com.muhammadakram.campus_placement_portal.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String role; // STUDENT, COMPANY, ya ADMIN

    private String otp;

    private String phone;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String profileImage;
}
