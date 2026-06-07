package com.nutritrack.dto.admin;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApproveNutritionistRequest {

    private Long nutritionistId;

    private Boolean approved;
}
