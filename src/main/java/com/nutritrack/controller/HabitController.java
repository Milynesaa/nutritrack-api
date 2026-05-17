package com.nutritrack.controller;

import com.nutritrack.dto.habit.CreateHabitRequest;
import com.nutritrack.dto.habit.HabitResponse;
import com.nutritrack.dto.response.ApiResponse;
import com.nutritrack.service.interfaces.HabitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;

    @PostMapping
    public ResponseEntity<ApiResponse<HabitResponse>> createHabit(
            @Valid @RequestBody CreateHabitRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.<HabitResponse>builder()
                        .success(true)
                        .message("Habit registered successfully")
                        .data(habitService.createHabit(request))
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HabitResponse>>> getHabits() {

        return ResponseEntity.ok(
                ApiResponse.<List<HabitResponse>>builder()
                        .success(true)
                        .message("Habits fetched successfully")
                        .data(habitService.getHabits())
                        .build()
        );
    }
}
