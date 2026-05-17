package com.nutritrack.dto.habit;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitResponse {

    private Long id;

    private Integer waterMl;

    private Integer sleepHours;

    private Integer exerciseMinutes;

    private LocalDate date;
}