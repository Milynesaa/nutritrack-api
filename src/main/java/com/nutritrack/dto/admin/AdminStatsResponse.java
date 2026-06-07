package com.nutritrack.dto.admin;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsResponse {

    private Integer totalPatients;

    private Integer totalNutritionists;

    private Integer pendingNutritionists;

    private List<String> patientNames;

    private List<NutritionistStats> nutritionistStats;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NutritionistStats {

        private Long id;

        private String name;

        private String email;

        private Integer patientCount;

        private Double efficiencyPercentage;

        private String status;
    }
}
