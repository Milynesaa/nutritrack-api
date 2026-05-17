package com.nutritrack.entity;

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

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}