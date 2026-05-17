package com.nutritrack.controller;

import com.nutritrack.dto.response.ApiResponse;
import com.nutritrack.dto.response.DashboardResponse;
import com.nutritrack.service.interfaces.DashboardService;
import com.nutritrack.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private static final org.slf4j.Logger log = LogUtil.getLogger(DashboardController.class);

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard(Authentication authentication) {
        String email = authentication.getName();
        log.info("Dashboard request for user: {}", email);

        DashboardResponse dashboard = dashboardService.getUserDashboard(email);

        return ResponseEntity.ok(
                ApiResponse.<DashboardResponse>builder()
                        .success(true)
                        .message("Dashboard retrieved successfully")
                        .data(dashboard)
                        .build()
        );
    }
}
