package com.nutritrack.service.interfaces;

import com.nutritrack.dto.ai.AIRequest;
import com.nutritrack.dto.ai.AIResponse;

public interface AIService {

    AIResponse askAI(AIRequest request);
}