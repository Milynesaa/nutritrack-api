package com.nutritrack.service.interfaces;

import com.nutritrack.dto.user.NutritionistProfileDTO;
import com.nutritrack.dto.user.UserResponse;

import java.util.List;

public interface NutritionistService {

    NutritionistProfileDTO getProfile();

    NutritionistProfileDTO updateProfile(NutritionistProfileDTO dto);

    List<UserResponse> getPatients();
}
