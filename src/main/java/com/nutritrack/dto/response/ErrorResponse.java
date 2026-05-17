package com.nutritrack.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private Boolean success;

    private String message;

    private Integer status;

    private LocalDateTime timestamp;
}