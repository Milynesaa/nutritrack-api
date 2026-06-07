package com.nutritrack.dto.admin;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateNutritionistRequest {

    private Long nutritionistId;

    private String name;
}
