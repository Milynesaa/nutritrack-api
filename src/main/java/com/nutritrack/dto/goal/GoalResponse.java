package com.nutritrack.dto.goal;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalResponse {

    private Long id;

    private String title;

    private String description;

    private Boolean completed;

    private LocalDateTime createdAt;
}
