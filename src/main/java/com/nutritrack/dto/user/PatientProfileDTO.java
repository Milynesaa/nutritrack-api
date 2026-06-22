package com.nutritrack.dto.user;

import com.nutritrack.entity.enums.ActivityLevel; // Import added
import com.nutritrack.entity.enums.Gender;       // Import added
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientProfileDTO {

    private Integer age;

    private Double weight;

    private Double height;

    private String goal;

    private Gender gender;         // Added gender
    private ActivityLevel activityLevel; // Added activityLevel
}