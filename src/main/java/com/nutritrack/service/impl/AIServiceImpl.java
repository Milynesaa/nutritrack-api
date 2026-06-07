package com.nutritrack.service.impl;

import com.nutritrack.dto.ai.*;
import com.nutritrack.entity.AIChatHistory;
import com.nutritrack.entity.User;
import com.nutritrack.repository.AIChatHistoryRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.service.interfaces.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {

    private final UserRepository userRepository;
    private final AIChatHistoryRepository historyRepository;
    private final ChatClient chatClient;

    @Override
    public AIResponse askAI(AIRequest request) {

        String response = chatClient.prompt()
                .user("Responde corto y saludable: " + request.getMessage())
                .call()
                .content();

        saveHistory(request.getMessage(), response);

        return AIResponse.builder()
                .response(response)
                .build();
    }

    @Override
    public List<AIChatHistoryResponse> getChatHistory() {

        return historyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private AIChatHistoryResponse mapToResponse(AIChatHistory history) {

        return AIChatHistoryResponse.builder()
                .id(history.getId())
                .question(history.getQuestion())
                .response(history.getResponse())
                .createdAt(history.getCreatedAt())
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