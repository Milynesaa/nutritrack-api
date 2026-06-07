package com.nutritrack.controller;

import com.nutritrack.dto.ai.AIChatHistoryResponse;
import com.nutritrack.dto.ai.AIRequest;
import com.nutritrack.dto.ai.AIResponse;
import com.nutritrack.dto.response.ApiResponse;
import com.nutritrack.service.interfaces.AIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<AIResponse>> chat(
            @Valid @RequestBody AIRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.<AIResponse>builder()
                        .success(true)
                        .message("AI response generated")
                        .data(aiService.askAI(request))
                        .build()
        );
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<AIChatHistoryResponse>>> getHistory() {

        return ResponseEntity.ok(
                ApiResponse.<List<AIChatHistoryResponse>>builder()
                        .success(true)
                        .message("Chat history retrieved successfully")
                        .data(aiService.getChatHistory())
                        .build()
        );
    }
}
