package com.nutritrack.service.impl;

import com.nutritrack.dto.goal.CreateGoalRequest;
import com.nutritrack.dto.goal.GoalResponse;
import com.nutritrack.entity.Goal;
import com.nutritrack.entity.User;
import com.nutritrack.repository.GoalRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.service.interfaces.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    @Override
    public GoalResponse createGoal(CreateGoalRequest request) {

        User user = getUser();

        Goal goal = Goal.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(request.getCompleted())
                .user(user)
                .build();

        goalRepository.save(goal);

        return map(goal);
    }

    @Override
    public List<GoalResponse> getGoals() {

        User user = getUser();

        return goalRepository.findByUser(user)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public GoalResponse updateGoal(Long id, CreateGoalRequest request) {

        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        goal.setTitle(request.getTitle());
        goal.setDescription(request.getDescription());
        goal.setCompleted(request.getCompleted());

        goalRepository.save(goal);

        return map(goal);
    }

    @Override
    public void deleteGoal(Long id) {

        goalRepository.deleteById(id);
    }

    @Override
    public GoalResponse toggleGoalCompletion(Long id) {

        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        goal.setCompleted(!goal.getCompleted());

        goalRepository.save(goal);

        return map(goal);
    }

    private GoalResponse map(Goal goal) {

        return GoalResponse.builder()
                .id(goal.getId())
                .title(goal.getTitle())
                .description(goal.getDescription())
                .completed(goal.getCompleted())
                .createdAt(LocalDateTime.now())
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
