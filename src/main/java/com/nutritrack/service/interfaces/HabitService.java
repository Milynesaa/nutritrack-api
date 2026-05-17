package com.nutritrack.service.interfaces;

import com.nutritrack.dto.habit.CreateHabitRequest;
import com.nutritrack.dto.habit.HabitResponse;

import java.util.List;

public interface HabitService {

    HabitResponse createHabit(CreateHabitRequest request);

    List<HabitResponse> getHabits();
}