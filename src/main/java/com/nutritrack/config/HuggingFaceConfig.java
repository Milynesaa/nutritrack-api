package com.nutritrack.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.huggingface.HuggingfaceChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HuggingFaceConfig {

    @Bean
    public ChatClient chatClient(HuggingfaceChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }
}
