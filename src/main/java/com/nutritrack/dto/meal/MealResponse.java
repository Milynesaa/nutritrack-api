package com.nutritrack.dto.meal;

import com.nutritrack.entity.enums.MealType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealResponse {

    private Long id;

    private String title;

    private String description;

    private Integer calories;

    private MealType type;

    private LocalDateTime createdAt;
}