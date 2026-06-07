package com.nutritrack.service.impl;

import com.nutritrack.dto.admin.AdminStatsResponse;
import com.nutritrack.dto.admin.ApproveNutritionistRequest;
import com.nutritrack.dto.admin.UpdateNutritionistRequest;
import com.nutritrack.dto.user.UserResponse;
import com.nutritrack.entity.Goal;
import com.nutritrack.entity.PatientNutritionist;
import com.nutritrack.entity.User;
import com.nutritrack.entity.enums.Role;
import com.nutritrack.entity.enums.UserStatus;
import com.nutritrack.repository.GoalRepository;
import com.nutritrack.repository.PatientNutritionistRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.service.interfaces.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PatientNutritionistRepository patientNutritionistRepository;
    private final GoalRepository goalRepository;

    @Override
    public List<UserResponse> getPendingNutritionists() {

        return userRepository.findByRoleAndStatus(Role.ROLE_NUTRITIONIST, UserStatus.PENDING)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public UserResponse approveNutritionist(ApproveNutritionistRequest request) {

        User nutritionist = userRepository.findById(request.getNutritionistId())
                .orElseThrow(() -> new RuntimeException("Nutritionist not found"));

        if (request.getApproved()) {
            nutritionist.setStatus(UserStatus.APPROVED);
        } else {
            nutritionist.setStatus(UserStatus.REJECTED);
        }

        userRepository.save(nutritionist);

        return mapToResponse(nutritionist);
    }

    @Override
    public List<UserResponse> getAllNutritionists() {

        return userRepository.findByRole(Role.ROLE_NUTRITIONIST)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AdminStatsResponse getAdminStats() {

        List<User> patients = userRepository.findByRole(Role.ROLE_PATIENT);
        List<User> nutritionists = userRepository.findByRole(Role.ROLE_NUTRITIONIST);
        List<User> pendingNutritionists = userRepository.findByRoleAndStatus(Role.ROLE_NUTRITIONIST, UserStatus.PENDING);

        List<String> patientNames = patients.stream()
                .map(User::getName)
                .collect(Collectors.toList());

        List<AdminStatsResponse.NutritionistStats> nutritionistStats = nutritionists.stream()
                .map(this::calculateNutritionistStats)
                .collect(Collectors.toList());

        return AdminStatsResponse.builder()
                .totalPatients(patients.size())
                .totalNutritionists(nutritionists.size())
                .pendingNutritionists(pendingNutritionists.size())
                .patientNames(patientNames)
                .nutritionistStats(nutritionistStats)
                .build();
    }

    @Override
    public UserResponse updateNutritionist(UpdateNutritionistRequest request) {

        User nutritionist = userRepository.findById(request.getNutritionistId())
                .orElseThrow(() -> new RuntimeException("Nutritionist not found"));

        nutritionist.setName(request.getName());
        userRepository.save(nutritionist);

        return mapToResponse(nutritionist);
    }

    @Override
    public void deleteNutritionist(Long nutritionistId) {

        User nutritionist = userRepository.findById(nutritionistId)
                .orElseThrow(() -> new RuntimeException("Nutritionist not found"));

        if (nutritionist.getRole() != Role.ROLE_NUTRITIONIST) {
            throw new RuntimeException("User is not a nutritionist");
        }

        userRepository.delete(nutritionist);
    }

    private AdminStatsResponse.NutritionistStats calculateNutritionistStats(User nutritionist) {

        List<PatientNutritionist> relations = patientNutritionistRepository.findByNutritionist(nutritionist);
        int patientCount = relations.size();

        // Calculate efficiency based on completed goals of all patients
        double efficiency = 0.0;
        if (patientCount > 0) {
            int totalGoals = 0;
            int completedGoals = 0;

            for (PatientNutritionist relation : relations) {
                User patient = relation.getPatient();
                List<Goal> patientGoals = goalRepository.findByUser(patient);
                
                totalGoals += patientGoals.size();
                completedGoals += (int) patientGoals.stream()
                        .filter(Goal::getCompleted)
                        .count();
            }

            if (totalGoals > 0) {
                efficiency = (completedGoals * 100.0) / totalGoals;
            }
        }

        return AdminStatsResponse.NutritionistStats.builder()
                .id(nutritionist.getId())
                .name(nutritionist.getName())
                .email(nutritionist.getEmail())
                .patientCount(patientCount)
                .efficiencyPercentage(efficiency)
                .status(nutritionist.getStatus().name())
                .build();
    }

    private UserResponse mapToResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
