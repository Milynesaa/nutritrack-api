package com.nutritrack.service.impl;

import com.nutritrack.dto.ai.*;
import com.nutritrack.entity.AIChatHistory;
import com.nutritrack.entity.User;
import com.nutritrack.repository.AIChatHistoryRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.service.interfaces.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {

    @Value("${huggingface.api.key}")
    private String apiKey;

    @Value("${huggingface.api.url}")
    private String apiUrl;

    private final UserRepository userRepository;
    private final AIChatHistoryRepository historyRepository;

    private final WebClient webClient = WebClient.create();

    @Override
    public AIResponse askAI(AIRequest request) {

        String response = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue(Map.of(
                        "inputs",
                        "Responde corto y saludable: " + request.getMessage()
                ))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        saveHistory(request.getMessage(), response);

        return AIResponse.builder()
                .response(response)
                .build();
    }

    private void saveHistory(String q, String r) {

        AIChatHistory history = AIChatHistory.builder()
                .question(q)
                .response(r)
                .createdAt(LocalDateTime.now())
                .build();

        historyRepository.save(history);
    }
}