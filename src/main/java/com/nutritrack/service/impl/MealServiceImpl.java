package com.nutritrack.service.impl;

import com.nutritrack.dto.meal.*;
import com.nutritrack.entity.Meal;
import com.nutritrack.entity.User;
import com.nutritrack.repository.MealRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.service.interfaces.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    @Override
    public MealResponse createMeal(CreateMealRequest request) {

        User user = getUser();

        Meal meal = Meal.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .calories(request.getCalories())
                .type(request.getType())
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        mealRepository.save(meal);

        return map(meal);
    }

    @Override
    public List<MealResponse> getMeals() {

        User user = getUser();

        return mealRepository.findByUser(user)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public void deleteMeal(Long id) {

        mealRepository.deleteById(id);
    }

    private MealResponse map(Meal meal) {

        return MealResponse.builder()
                .id(meal.getId())
                .title(meal.getTitle())
                .description(meal.getDescription())
                .calories(meal.getCalories())
                .type(meal.getType())
                .createdAt(meal.getCreatedAt())
                .build();
    }

    private User getUser() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
