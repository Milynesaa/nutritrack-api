package com.nutritrack.dto.user;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NutritionistProfileDTO {

    private String specialization;

    private String description;
}
