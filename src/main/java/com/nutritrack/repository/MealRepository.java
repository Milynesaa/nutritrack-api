package com.nutritrack.repository;

import com.nutritrack.entity.Meal;
import com.nutritrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {

    List<Meal> findByUser(User user);
}