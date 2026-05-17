package com.nutritrack.service.interfaces;

import com.nutritrack.dto.response.DashboardResponse;

public interface DashboardService {
    
    DashboardResponse getUserDashboard(String email);
}
