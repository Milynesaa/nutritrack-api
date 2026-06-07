package com.nutritrack.controller;

import com.nutritrack.dto.response.ApiResponse;
import com.nutritrack.dto.user.NutritionistProfileDTO;
import com.nutritrack.dto.user.UserResponse;
import com.nutritrack.service.interfaces.NutritionistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nutritionist")
@RequiredArgsConstructor
public class NutritionistController {

    private final NutritionistService nutritionistService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<NutritionistProfileDTO>> getProfile() {

        return ResponseEntity.ok(
                ApiResponse.<NutritionistProfileDTO>builder()
                        .success(true)
                        .message("Nutritionist profile fetched successfully")
                        .data(nutritionistService.getProfile())
                        .build()
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<NutritionistProfileDTO>> updateProfile(
            @RequestBody NutritionistProfileDTO dto
    ) {

        return ResponseEntity.ok(
                ApiResponse.<NutritionistProfileDTO>builder()
                        .success(true)
                        .message("Nutritionist profile updated successfully")
                        .data(nutritionistService.updateProfile(dto))
                        .build()
        );
    }

    @GetMapping("/patients")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getPatients() {

        return ResponseEntity.ok(
                ApiResponse.<List<UserResponse>>builder()
                        .success(true)
                        .message("Patients fetched successfully")
                        .data(nutritionistService.getPatients())
                        .build()
        );
    }

    @GetMapping("/patients/{patientId}/meals")
    public ResponseEntity<ApiResponse<List<Object>>> getPatientMeals(
            @PathVariable Long patientId
    ) {

        return ResponseEntity.ok(
                ApiResponse.<List<Object>>builder()
                        .success(true)
                        .message("Patient meals fetched successfully")
                        .data((List<Object>) nutritionistService.getPatientMeals(patientId))
                        .build()
        );
    }

    @GetMapping("/patients/{patientId}/habits")
    public ResponseEntity<ApiResponse<List<Object>>> getPatientHabits(
            @PathVariable Long patientId
    ) {

        return ResponseEntity.ok(
                ApiResponse.<List<Object>>builder()
                        .success(true)
                        .message("Patient habits fetched successfully")
                        .data((List<Object>) nutritionistService.getPatientHabits(patientId))
                        .build()
        );
    }

    @GetMapping("/patients/{patientId}/goals")
    public ResponseEntity<ApiResponse<List<Object>>> getPatientGoals(
            @PathVariable Long patientId
    ) {

        return ResponseEntity.ok(
                ApiResponse.<List<Object>>builder()
                        .success(true)
                        .message("Patient goals fetched successfully")
                        .data((List<Object>) nutritionistService.getPatientGoals(patientId))
                        .build()
        );
    }
}