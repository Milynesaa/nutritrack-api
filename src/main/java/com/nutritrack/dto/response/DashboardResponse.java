package com.nutritrack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    
    private Long userId;
    private String name;
    private String email;
    private String role;
    
    private Integer totalMeals;
    private Integer totalHabits;
    private Integer mealsToday;
    
    private LocalDateTime lastMealDate;
    private LocalDate lastHabitDate;
    
    private String message;
}
