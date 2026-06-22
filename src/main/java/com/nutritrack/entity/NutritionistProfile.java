package com.nutritrack.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionistProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String specialization;

    @Column(length = 2000)
    private String description;

    @Column(unique = true)
    private String licenseNumber;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
