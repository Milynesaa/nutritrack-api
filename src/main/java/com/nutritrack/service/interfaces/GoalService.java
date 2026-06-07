package com.nutritrack.service.interfaces;

import com.nutritrack.dto.goal.CreateGoalRequest;
import com.nutritrack.dto.goal.GoalResponse;

import java.util.List;

public interface GoalService {

    GoalResponse createGoal(CreateGoalRequest request);

    List<GoalResponse> getGoals();

    GoalResponse updateGoal(Long id, CreateGoalRequest request);

    void deleteGoal(Long id);

    GoalResponse toggleGoalCompletion(Long id);
}
