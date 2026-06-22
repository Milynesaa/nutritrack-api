package com.nutritrack.service.interfaces;

import com.nutritrack.dto.goal.GoalResponse;     // Import added
import com.nutritrack.dto.habit.HabitResponse;   // Import added
import com.nutritrack.dto.meal.MealResponse;     // Import added
import com.nutritrack.dto.user.NutritionistProfileDTO;
import com.nutritrack.dto.user.UserResponse;

import java.util.List;

public interface NutritionistService {

    NutritionistProfileDTO getProfile();

    NutritionistProfileDTO updateProfile(NutritionistProfileDTO dto);

    List<UserResponse> getPatients();

    UserResponse getPatientDetails(Long patientId);

    List<MealResponse> getPatientMeals(Long patientId);   // Changed return type
    List<HabitResponse> getPatientHabits(Long patientId); // Changed return type
    List<GoalResponse> getPatientGoals(Long patientId);   // Changed return type
}