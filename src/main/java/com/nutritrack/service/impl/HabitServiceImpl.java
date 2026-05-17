package com.nutritrack.service.impl;

import com.nutritrack.dto.habit.*;
import com.nutritrack.entity.Habit;
import com.nutritrack.entity.User;
import com.nutritrack.repository.HabitRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.service.interfaces.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;

    @Override
    public HabitResponse createHabit(CreateHabitRequest request) {

        User user = getUser();

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

        User user = getUser();

        return habitRepository.findByUser(user)
                .stream()
                .map(this::map)
                .toList();
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

    private User getUser() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
