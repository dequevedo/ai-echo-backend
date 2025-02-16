package com.pocspringboot.service;

import com.pocspringboot.model.request.ChatRequest;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatService {

    private final OpenAiChatModel chatModel;

    public ChatService(@Value("${openai.api-key}") String apiKey) {
        this.chatModel = OpenAiChatModel.withApiKey(apiKey);
    }

    public String chat(ChatRequest request) {
        return chatModel.generate(request.getMessage());
    }

}
