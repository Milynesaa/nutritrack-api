package com.nutritrack.service.impl;

import com.nutritrack.dto.response.DashboardResponse;
import com.nutritrack.entity.Habit;
import com.nutritrack.entity.Meal;
import com.nutritrack.entity.User;
import com.nutritrack.repository.HabitRepository;
import com.nutritrack.repository.MealRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.service.interfaces.DashboardService;
import com.nutritrack.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final MealRepository mealRepository;
    private final HabitRepository habitRepository;
    
    private static final org.slf4j.Logger log = LogUtil.getLogger(DashboardServiceImpl.class);

    @Override
    public DashboardResponse getUserDashboard(String email) {
        log.info("Fetching dashboard for user: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", email);
                    return new RuntimeException("User not found");
                });

        List<Meal> meals = mealRepository.findByUser(user);
        List<Habit> habits = habitRepository.findByUser(user);
        
        LocalDate today = LocalDate.now();
        int mealsToday = (int) meals.stream()
                .filter(meal -> meal.getCreatedAt().toLocalDate().equals(today))
                .count();

        LocalDateTime lastMealDate = meals.stream()
                .map(Meal::getCreatedAt)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        LocalDate lastHabitDate = habits.stream()
                .map(Habit::getDate)
                .max(LocalDate::compareTo)
                .orElse(null);

        log.info("Dashboard fetched successfully for user: {}", email);

        return DashboardResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .totalMeals(meals.size())
                .totalHabits(habits.size())
                .mealsToday(mealsToday)
                .lastMealDate(lastMealDate)
                .lastHabitDate(lastHabitDate)
                .message("Dashboard data retrieved successfully")
                .build();
    }
}
