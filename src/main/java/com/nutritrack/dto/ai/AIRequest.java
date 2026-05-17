package com.nutritrack.dto.ai;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AIRequest {

    @NotBlank(message = "Message cannot be empty")
    private String message;
}
