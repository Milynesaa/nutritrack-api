package com.nutritrack.service.interfaces;

import com.nutritrack.dto.admin.AdminStatsResponse;
import com.nutritrack.dto.admin.ApproveNutritionistRequest;
import com.nutritrack.dto.admin.UpdateNutritionistRequest;
import com.nutritrack.dto.response.PageResponse;
import com.nutritrack.dto.user.UserResponse;
import com.nutritrack.entity.enums.UserStatus;

import java.util.List;

public interface AdminService {

    PageResponse<UserResponse> getPendingNutritionists(int page, int size);

    UserResponse approveNutritionist(ApproveNutritionistRequest request);

    PageResponse<UserResponse> getAllNutritionists(int page, int size);

    AdminStatsResponse getAdminStats();

    UserResponse updateNutritionist(UpdateNutritionistRequest request);

    void deleteNutritionist(Long nutritionistId);
}
