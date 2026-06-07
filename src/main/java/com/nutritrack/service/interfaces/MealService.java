package com.nutritrack.service.interfaces;

import com.nutritrack.dto.meal.CreateMealRequest;
import com.nutritrack.dto.meal.MealResponse;

import java.util.List;

public interface MealService {

    MealResponse createMeal(CreateMealRequest request);

    List<MealResponse> getMeals();

    MealResponse updateMeal(Long id, CreateMealRequest request);

    void deleteMeal(Long id);
}
