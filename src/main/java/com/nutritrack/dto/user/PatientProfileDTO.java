package com.nutritrack.dto.user;

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
}