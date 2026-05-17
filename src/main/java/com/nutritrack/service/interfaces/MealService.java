package com.nutritrack.service.interfaces;

import com.nutritrack.dto.meal.CreateMealRequest;
import com.nutritrack.dto.meal.MealResponse;

import java.util.List;

public interface MealService {

    MealResponse createMeal(CreateMealRequest request);

    List<MealResponse> getMeals();

    void deleteMeal(Long id);
}
