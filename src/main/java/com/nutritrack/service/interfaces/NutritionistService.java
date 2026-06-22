package com.nutritrack.service.interfaces;

import com.nutritrack.dto.user.NutritionistProfileDTO;
import com.nutritrack.dto.user.UserResponse;

import java.util.List;

public interface NutritionistService {

    NutritionistProfileDTO getProfile();

    NutritionistProfileDTO updateProfile(NutritionistProfileDTO dto);

    List<UserResponse> getPatients();

    UserResponse getPatientDetails(Long patientId); // Added this method

    List<?> getPatientMeals(Long patientId);

    List<?> getPatientHabits(Long patientId);

    List<?> getPatientGoals(Long patientId);
}