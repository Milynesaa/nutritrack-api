package com.nutritrack.dto.ai;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIChatHistoryResponse {

    private Long id;

    private String question;

    private String response;

    private LocalDateTime createdAt;
}
