package com.nutritrack.service.interfaces;

import com.nutritrack.dto.admin.AdminStatsResponse;
import com.nutritrack.dto.admin.ApproveNutritionistRequest;
import com.nutritrack.dto.admin.UpdateNutritionistRequest;
import com.nutritrack.dto.user.UserResponse;
import com.nutritrack.entity.enums.UserStatus;

import java.util.List;

public interface AdminService {

    List<UserResponse> getPendingNutritionists();

    UserResponse approveNutritionist(ApproveNutritionistRequest request);

    List<UserResponse> getAllNutritionists();

    AdminStatsResponse getAdminStats();

    UserResponse updateNutritionist(UpdateNutritionistRequest request);

    void deleteNutritionist(Long nutritionistId);
}
