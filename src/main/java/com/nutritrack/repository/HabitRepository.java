package com.nutritrack.repository;

import com.nutritrack.entity.Habit;
import com.nutritrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRepository extends JpaRepository<Habit, Long> {

    List<Habit> findByUser(User user);
}