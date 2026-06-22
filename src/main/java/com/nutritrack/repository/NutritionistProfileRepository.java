package com.nutritrack.repository;

import com.nutritrack.entity.NutritionistProfile;
import com.nutritrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NutritionistProfileRepository extends JpaRepository<NutritionistProfile, Long> {
    Optional<NutritionistProfile> findByUser(User user);
}