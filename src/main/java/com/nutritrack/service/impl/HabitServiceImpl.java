package com.nutritrack.service.impl;

import com.nutritrack.dto.habit.*;
import com.nutritrack.entity.Habit;
import com.nutritrack.entity.User;
import com.nutritrack.repository.HabitRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.service.CurrentUserService; // Import CurrentUserService
import com.nutritrack.service.interfaces.HabitService;
import com.nutritrack.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder; // Keep for now, might be removed later

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository; // Keep for now, might be removed if not used elsewhere
    private final CurrentUserService currentUserService; // Inject CurrentUserService

    @Override
    public HabitResponse createHabit(CreateHabitRequest request) {

        User user = currentUserService.getCurrentUser(); // Use CurrentUserService

        Habit habit = Habit.builder()
                .waterMl(request.getWaterMl())
                .sleepHours(request.getSleepHours())
                .exerciseMinutes(request.getExerciseMinutes())
                .date(LocalDate.now())
                .user(user)
                .build();

        habitRepository.save(habit);

        return map(habit);
    }

    @Override
    public List<HabitResponse> getHabits() {

        User user = currentUserService.getCurrentUser(); // Use CurrentUserService

        return habitRepository.findByUser(user)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public HabitResponse updateHabit(Long id, CreateHabitRequest request) {
        User currentUser = currentUserService.getCurrentUser(); // Use CurrentUserService

        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habit not found"));

        // Check ownership
        if (!habit.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not authorized to update this habit.");
        }

        habit.setWaterMl(request.getWaterMl());
        habit.setSleepHours(request.getSleepHours());
        habit.setExerciseMinutes(request.getExerciseMinutes());

        habitRepository.save(habit);

        return map(habit);
    }

    @Override
    public void deleteHabit(Long id) {
        User currentUser = currentUserService.getCurrentUser(); // Use CurrentUserService

        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habit not found"));

        // Check ownership
        if (!habit.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not authorized to delete this habit.");
        }

        habitRepository.deleteById(id);
    }

    private HabitResponse map(Habit h) {

        return HabitResponse.builder()
                .id(h.getId())
                .waterMl(h.getWaterMl())
                .sleepHours(h.getSleepHours())
                .exerciseMinutes(h.getExerciseMinutes())
                .date(h.getDate())
                .build();
    }
}