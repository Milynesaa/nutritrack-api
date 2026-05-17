package com.nutritrack.repository;

import com.nutritrack.entity.Goal;
import com.nutritrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByUser(User user);
}