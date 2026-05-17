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
}
