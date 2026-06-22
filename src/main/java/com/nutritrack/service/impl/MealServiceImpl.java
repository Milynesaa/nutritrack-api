package com.nutritrack.service.impl;

import com.nutritrack.dto.meal.*;
import com.nutritrack.entity.Meal;
import com.nutritrack.entity.User;
import com.nutritrack.repository.MealRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.service.CurrentUserService; // Import CurrentUserService
import com.nutritrack.service.interfaces.MealService;
import com.nutritrack.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder; // Keep for now, might be removed later
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final UserRepository userRepository; // Keep for now, might be removed if not used elsewhere
    private final CurrentUserService currentUserService; // Inject CurrentUserService

    @Override
    public MealResponse createMeal(CreateMealRequest request) {

        User user = currentUserService.getCurrentUser(); // Use CurrentUserService

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

        User user = currentUserService.getCurrentUser(); // Use CurrentUserService

        return mealRepository.findByUser(user)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public MealResponse updateMeal(Long id, CreateMealRequest request) {
        User currentUser = currentUserService.getCurrentUser(); // Use CurrentUserService

        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meal not found"));

        // Check ownership
        if (!meal.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not authorized to update this meal.");
        }

        meal.setTitle(request.getTitle());
        meal.setDescription(request.getDescription());
        meal.setCalories(request.getCalories());
        meal.setType(request.getType());

        mealRepository.save(meal);

        return map(meal);
    }

    @Override
    public void deleteMeal(Long id) {
        User currentUser = currentUserService.getCurrentUser(); // Use CurrentUserService

        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meal not found"));

        // Check ownership
        if (!meal.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not authorized to delete this meal.");
        }

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
}