package com.nutritrack.dto.meal;

import com.nutritrack.entity.enums.MealType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMealRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Calories are required")
    private Integer calories;

    @NotNull(message = "Meal type is required")
    private MealType type;
}