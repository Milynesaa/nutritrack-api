package com.nutritrack.controller;

import com.nutritrack.dto.admin.AdminStatsResponse;
import com.nutritrack.dto.admin.ApproveNutritionistRequest;
import com.nutritrack.dto.admin.UpdateNutritionistRequest;
import com.nutritrack.dto.response.ApiResponse;
import com.nutritrack.dto.user.UserResponse;
import com.nutritrack.service.interfaces.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdminStatsResponse>> getAdminStats() {

        return ResponseEntity.ok(
                ApiResponse.<AdminStatsResponse>builder()
                        .success(true)
                        .message("Admin stats retrieved successfully")
                        .data(adminService.getAdminStats())
                        .build()
        );
    }

    @GetMapping("/nutritionists/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getPendingNutritionists() {

        return ResponseEntity.ok(
                ApiResponse.<List<UserResponse>>builder()
                        .success(true)
                        .message("Pending nutritionists retrieved successfully")
                        .data(adminService.getPendingNutritionists())
                        .build()
        );
    }

    @PostMapping("/nutritionists/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> approveNutritionist(
            @RequestBody ApproveNutritionistRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("Nutritionist status updated successfully")
                        .data(adminService.approveNutritionist(request))
                        .build()
        );
    }

    @GetMapping("/nutritionists")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllNutritionists() {

        return ResponseEntity.ok(
                ApiResponse.<List<UserResponse>>builder()
                        .success(true)
                        .message("All nutritionists retrieved successfully")
                        .data(adminService.getAllNutritionists())
                        .build()
        );
    }

    @PutMapping("/nutritionists")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateNutritionist(
            @RequestBody UpdateNutritionistRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("Nutritionist updated successfully")
                        .data(adminService.updateNutritionist(request))
                        .build()
        );
    }

    @DeleteMapping("/nutritionists/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteNutritionist(
            @PathVariable Long id
    ) {

        adminService.deleteNutritionist(id);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Nutritionist deleted successfully")
                        .data("Deleted")
                        .build()
        );
    }
}
