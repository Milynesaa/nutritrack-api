package com.nutritrack.service.interfaces;

import com.nutritrack.dto.ai.AIChatHistoryResponse;
import com.nutritrack.dto.ai.AIRequest;
import com.nutritrack.dto.ai.AIResponse;

import java.util.List;

public interface AIService {

    AIResponse askAI(AIRequest request);

    List<AIChatHistoryResponse> getChatHistory();
}