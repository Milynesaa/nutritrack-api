package com.nutritrack.controller;

import com.nutritrack.dto.meal.CreateMealRequest;
import com.nutritrack.dto.meal.MealResponse;
import com.nutritrack.dto.response.ApiResponse;
import com.nutritrack.service.interfaces.MealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @PostMapping
    public ResponseEntity<ApiResponse<MealResponse>> createMeal(
            @Valid @RequestBody CreateMealRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.<MealResponse>builder()
                        .success(true)
                        .message("Meal created successfully")
                        .data(mealService.createMeal(request))
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MealResponse>>> getMeals() {

        return ResponseEntity.ok(
                ApiResponse.<List<MealResponse>>builder()
                        .success(true)
                        .message("Meals fetched successfully")
                        .data(mealService.getMeals())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteMeal(
            @PathVariable Long id
    ) {

        mealService.deleteMeal(id);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Meal deleted successfully")
                        .data("Deleted")
                        .build()
        );
    }
}
