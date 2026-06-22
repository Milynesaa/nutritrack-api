package com.nutritrack.service.impl;

import com.nutritrack.dto.goal.GoalResponse;
import com.nutritrack.dto.habit.HabitResponse;
import com.nutritrack.dto.meal.MealResponse;
import com.nutritrack.dto.user.NutritionistProfileDTO;
import com.nutritrack.dto.user.UserResponse;
import com.nutritrack.entity.Goal;
import com.nutritrack.entity.Habit;
import com.nutritrack.entity.Meal;
import com.nutritrack.entity.NutritionistProfile;
import com.nutritrack.entity.PatientNutritionist;
import com.nutritrack.entity.User;
import com.nutritrack.entity.enums.Role;
import com.nutritrack.exception.ResourceNotFoundException;
import com.nutritrack.exception.UnauthorizedException;
import com.nutritrack.repository.GoalRepository;
import com.nutritrack.repository.HabitRepository;
import com.nutritrack.repository.MealRepository;
import com.nutritrack.repository.NutritionistProfileRepository;
import com.nutritrack.repository.PatientNutritionistRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.service.CurrentUserService;
import com.nutritrack.service.interfaces.NutritionistService;
import com.nutritrack.util.UserMapper; // Import UserMapper
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NutritionistServiceImpl implements NutritionistService {

    private final UserRepository userRepository;
    private final PatientNutritionistRepository patientNutritionistRepository;
    private final MealRepository mealRepository;
    private final HabitRepository habitRepository;
    private final GoalRepository goalRepository;
    private final NutritionistProfileRepository nutritionistProfileRepository;
    private final CurrentUserService currentUserService;
    private final UserMapper userMapper; // Inject UserMapper

    @Override
    public NutritionistProfileDTO getProfile() {
        User nutritionist = currentUserService.getCurrentUser();
        NutritionistProfile profile = nutritionistProfileRepository.findByUser(nutritionist)
                .orElseThrow(() -> new ResourceNotFoundException("Nutritionist profile not found"));

        return mapToNutritionistProfileDTO(profile);
    }

    @Override
    public NutritionistProfileDTO updateProfile(NutritionistProfileDTO dto) {
        User nutritionist = currentUserService.getCurrentUser();
        NutritionistProfile profile = nutritionistProfileRepository.findByUser(nutritionist)
                .orElseThrow(() -> new ResourceNotFoundException("Nutritionist profile not found"));

        profile.setSpecialization(dto.getSpecialization());
        profile.setDescription(dto.getDescription());
        // Assuming licenseNumber is not updated via DTO for security/process reasons, or needs separate handling
        // profile.setLicenseNumber(dto.getLicenseNumber());

        nutritionistProfileRepository.save(profile);
        return mapToNutritionistProfileDTO(profile);
    }

    @Override
    public List<UserResponse> getPatients() {
        User nutritionist = currentUserService.getCurrentUser();
        List<PatientNutritionist> relations = patientNutritionistRepository.findByNutritionist(nutritionist);
        
        return relations.stream()
                .map(PatientNutritionist::getPatient)
                .map(userMapper::mapToUserResponse) // Use UserMapper
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getPatientDetails(Long patientId) {
        User nutritionist = currentUserService.getCurrentUser();
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));

        // Ensure the nutritionist is assigned to this patient
        patientNutritionistRepository.findByNutritionistAndPatient(nutritionist, patient)
                .orElseThrow(() -> new UnauthorizedException("Nutritionist is not assigned to patient with ID: " + patientId));

        return userMapper.mapToUserResponse(patient); // Use UserMapper
    }

    @Override
    public List<MealResponse> getPatientMeals(Long patientId) {
        User nutritionist = currentUserService.getCurrentUser();
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));

        // Ensure the nutritionist is assigned to this patient
        patientNutritionistRepository.findByNutritionistAndPatient(nutritionist, patient)
                .orElseThrow(() -> new UnauthorizedException("Nutritionist is not assigned to patient with ID: " + patientId));
        
        return mealRepository.findByUser(patient)
                .stream()
                .map(this::mapToMealResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<HabitResponse> getPatientHabits(Long patientId) {
        User nutritionist = currentUserService.getCurrentUser();
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));

        // Ensure the nutritionist is assigned to this patient
        patientNutritionistRepository.findByNutritionistAndPatient(nutritionist, patient)
                .orElseThrow(() -> new UnauthorizedException("Nutritionist is not assigned to patient with ID: " + patientId));

        return habitRepository.findByUser(patient)
                .stream()
                .map(this::mapToHabitResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<GoalResponse> getPatientGoals(Long patientId) {
        User nutritionist = currentUserService.getCurrentUser();
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));

        // Ensure the nutritionist is assigned to this patient
        patientNutritionistRepository.findByNutritionistAndPatient(nutritionist, patient)
                .orElseThrow(() -> new UnauthorizedException("Nutritionist is not assigned to patient with ID: " + patientId));

        return goalRepository.findByUser(patient)
                .stream()
                .map(this::mapToGoalResponse)
                .collect(Collectors.toList());
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

    private HabitResponse mapToHabitResponse(Habit habit) {
        return HabitResponse.builder()
                .id(habit.getId())
                .waterMl(habit.getWaterMl())
                .sleepHours(habit.getSleepHours())
                .exerciseMinutes(habit.getExerciseMinutes())
                .date(habit.getDate())
                .build();
    }

    private GoalResponse mapToGoalResponse(Goal goal) {
        return GoalResponse.builder()
                .id(goal.getId())
                .title(goal.getTitle())
                .description(goal.getDescription())
                .completed(goal.getCompleted())
                .createdAt(goal.getCreatedAt())
                .build();
    }

    private NutritionistProfileDTO mapToNutritionistProfileDTO(NutritionistProfile profile) {
        return NutritionistProfileDTO.builder()
                .specialization(profile.getSpecialization())
                .description(profile.getDescription())
                // .licenseNumber(profile.getLicenseNumber()) // Assuming licenseNumber is not exposed via DTO
                .build();
    }
}