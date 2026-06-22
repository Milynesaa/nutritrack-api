package com.nutritrack.entity;

import com.nutritrack.entity.enums.ActivityLevel; // Import added
import com.nutritrack.entity.enums.Gender;       // Import added
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer age;

    private Double weight;

    private Double height;

    private String goal;

    @Enumerated(EnumType.STRING) // Added for Gender enum
    private Gender gender;

    @Enumerated(EnumType.STRING) // Added for ActivityLevel enum
    private ActivityLevel activityLevel;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}