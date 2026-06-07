package com.nutritrack.controller;

import com.nutritrack.dto.goal.CreateGoalRequest;
import com.nutritrack.dto.goal.GoalResponse;
import com.nutritrack.dto.response.ApiResponse;
import com.nutritrack.service.interfaces.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @PostMapping
    public ResponseEntity<ApiResponse<GoalResponse>> createGoal(
            @Valid @RequestBody CreateGoalRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.<GoalResponse>builder()
                        .success(true)
                        .message("Goal created successfully")
                        .data(goalService.createGoal(request))
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GoalResponse>>> getGoals() {

        return ResponseEntity.ok(
                ApiResponse.<List<GoalResponse>>builder()
                        .success(true)
                        .message("Goals fetched successfully")
                        .data(goalService.getGoals())
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GoalResponse>> updateGoal(
            @PathVariable Long id,
            @Valid @RequestBody CreateGoalRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.<GoalResponse>builder()
                        .success(true)
                        .message("Goal updated successfully")
                        .data(goalService.updateGoal(id, request))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteGoal(
            @PathVariable Long id
    ) {

        goalService.deleteGoal(id);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Goal deleted successfully")
                        .data("Deleted")
                        .build()
        );
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<GoalResponse>> toggleGoalCompletion(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponse.<GoalResponse>builder()
                        .success(true)
                        .message("Goal completion toggled successfully")
                        .data(goalService.toggleGoalCompletion(id))
                        .build()
        );
    }
}
