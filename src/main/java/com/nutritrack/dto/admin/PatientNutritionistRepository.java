package com.nutritrack.repository;

import com.nutritrack.entity.PatientNutritionist;
import com.nutritrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientNutritionistRepository extends JpaRepository<PatientNutritionist, Long> {

    List<PatientNutritionist> findByNutritionist(User nutritionist);

    Optional<PatientNutritionist> findByPatient(User patient);
}
