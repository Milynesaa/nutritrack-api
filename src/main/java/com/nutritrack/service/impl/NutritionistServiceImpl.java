package com.nutritrack.service.impl;

import com.nutritrack.dto.user.NutritionistProfileDTO;
import com.nutritrack.dto.user.UserResponse;
import com.nutritrack.service.interfaces.NutritionistService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NutritionistServiceImpl implements NutritionistService {

    @Override
    public NutritionistProfileDTO getProfile() {
        return null;
    }

    @Override
    public NutritionistProfileDTO updateProfile(NutritionistProfileDTO dto) {
        return dto;
    }

    @Override
    public List<UserResponse> getPatients() {
        return List.of();
    }
}
