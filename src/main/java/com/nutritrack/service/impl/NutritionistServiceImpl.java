package com.nutritrack.service.impl;

import com.nutritrack.dto.meal.MealResponse;
import com.nutritrack.dto.goal.GoalResponse;
import com.nutritrack.dto.habit.HabitResponse;
import com.nutritrack.dto.user.NutritionistProfileDTO;
import com.nutritrack.dto.user.UserResponse;
import com.nutritrack.entity.Meal;
import com.nutritrack.entity.PatientNutritionist;
import com.nutritrack.entity.User;
import com.nutritrack.entity.enums.Role;
import com.nutritrack.repository.MealRepository;
import com.nutritrack.repository.PatientNutritionistRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.service.interfaces.NutritionistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NutritionistServiceImpl implements NutritionistService {

    private final UserRepository userRepository;
    private final PatientNutritionistRepository patientNutritionistRepository;
    private final MealRepository mealRepository;

    @Override
    public NutritionistProfileDTO getProfile() {
        // TODO: Implement profile retrieval
        return null;
    }

    @Override
    public NutritionistProfileDTO updateProfile(NutritionistProfileDTO dto) {
        // TODO: Implement profile update
        return dto;
    }

    @Override
    public List<UserResponse> getPatients() {
        User nutritionist = getCurrentNutritionist();
        List<PatientNutritionist> relations = patientNutritionistRepository.findByNutritionist(nutritionist);
        
        return relations.stream()
                .map(PatientNutritionist::getPatient)
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getPatientDetails(Long patientId) {
        User nutritionist = getCurrentNutritionist();
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));

        // Ensure the nutritionist is assigned to this patient
        patientNutritionistRepository.findByNutritionistAndPatient(nutritionist, patient)
                .orElseThrow(() -> new RuntimeException("Nutritionist is not assigned to patient with ID: " + patientId));

        return mapToUserResponse(patient);
    }

    @Override
    public List<?> getPatientMeals(Long patientId) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        return mealRepository.findByUser(patient)
                .stream()
                .map(this::mapToMealResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<?> getPatientHabits(Long patientId) {
        // TODO: Implement habit retrieval
        return List.of();
    }

    @Override
    public List<?> getPatientGoals(Long patientId) {
        // TODO: Implement goal retrieval
        return List.of();
    }

    private User getCurrentNutritionist() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nutritionist not found"));
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private MealResponse mapToMealResponse(Meal meal) {
        return MealResponse.builder()
                .id(meal.getId())
                .title(meal.getTitle())
                .description(meal.getDescription())
                .calories(meal.getCalories())
                .type(meal.getType())
                .createdAt(meal.getCreatedAt())
                .build();
    }
}