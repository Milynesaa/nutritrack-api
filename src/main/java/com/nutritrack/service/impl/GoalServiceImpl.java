package com.nutritrack.service.impl;

import com.nutritrack.dto.goal.CreateGoalRequest;
import com.nutritrack.dto.goal.GoalResponse;
import com.nutritrack.entity.Goal;
import com.nutritrack.entity.User;
import com.nutritrack.repository.GoalRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.service.CurrentUserService; // Import CurrentUserService
import com.nutritrack.service.interfaces.GoalService;
import com.nutritrack.exception.ResourceNotFoundException;
import com.nutritrack.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder; // Keep for now, might be removed later

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository; // Keep for now, might be removed if not used elsewhere
    private final CurrentUserService currentUserService; // Inject CurrentUserService

    @Override
    public GoalResponse createGoal(CreateGoalRequest request) {

        User user = currentUserService.getCurrentUser(); // Use CurrentUserService

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

        User user = currentUserService.getCurrentUser(); // Use CurrentUserService

        return goalRepository.findByUser(user)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public GoalResponse updateGoal(Long id, CreateGoalRequest request) {
        User currentUser = currentUserService.getCurrentUser(); // Use CurrentUserService

        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        // Check ownership
        if (!goal.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not authorized to update this goal.");
        }

        goal.setTitle(request.getTitle());
        goal.setDescription(request.getDescription());
        goal.setCompleted(request.getCompleted());

        goalRepository.save(goal);

        return map(goal);
    }

    @Override
    public void deleteGoal(Long id) {
        User currentUser = currentUserService.getCurrentUser(); // Use CurrentUserService

        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        // Check ownership
        if (!goal.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not authorized to delete this goal.");
        }

        goalRepository.deleteById(id);
    }

    @Override
    public GoalResponse toggleGoalCompletion(Long id) {
        User currentUser = currentUserService.getCurrentUser(); // Use CurrentUserService

        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        // Check ownership
        if (!goal.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not authorized to toggle completion for this goal.");
        }

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
                .createdAt(goal.getCreatedAt())
                .build();
    }
}