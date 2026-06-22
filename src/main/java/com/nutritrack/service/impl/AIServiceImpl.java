package com.nutritrack.service.impl;

import com.nutritrack.dto.ai.*;
import com.nutritrack.entity.AIChatHistory;
import com.nutritrack.entity.User;
import com.nutritrack.repository.AIChatHistoryRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.service.CurrentUserService; // Import CurrentUserService
import com.nutritrack.service.interfaces.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.core.context.SecurityContextHolder; // Keep for now, might be removed later
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {

    private final UserRepository userRepository; // Keep for now, might be removed if not used elsewhere
    private final AIChatHistoryRepository historyRepository;
    private final ChatClient chatClient;
    private final CurrentUserService currentUserService; // Inject CurrentUserService

    @Override
    public AIResponse askAI(AIRequest request) {
        User currentUser = currentUserService.getCurrentUser(); // Use CurrentUserService

        String response = chatClient.prompt()
                .user("Responde corto y saludable: " + request.getMessage())
                .call()
                .content();

        saveHistory(request.getMessage(), response, currentUser); // Pass current user

        return AIResponse.builder()
                .response(response)
                .build();
    }

    @Override
    public List<AIChatHistoryResponse> getChatHistory() {
        User currentUser = currentUserService.getCurrentUser(); // Use CurrentUserService

        return historyRepository.findByUser(currentUser) // Filter by current user
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

    private void saveHistory(String q, String r, User user) { // Accept User parameter

        AIChatHistory history = AIChatHistory.builder()
                .question(q)
                .response(r)
                .createdAt(LocalDateTime.now())
                .user(user) // Associate with user
                .build();

        historyRepository.save(history);
    }
}