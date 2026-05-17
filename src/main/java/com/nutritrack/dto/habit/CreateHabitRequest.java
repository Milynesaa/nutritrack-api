package com.nutritrack.dto.habit;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateHabitRequest {

    @NotNull
    private Integer waterMl;

    @NotNull
    private Integer sleepHours;

    @NotNull
    private Integer exerciseMinutes;
}