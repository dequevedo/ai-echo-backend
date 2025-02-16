package com.pocspringboot.service;

import com.pocspringboot.model.request.ChatRequest;
import com.pocspringboot.model.response.ChatResponse;
import com.pocspringboot.model.response.VoiceToTextResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

@Slf4j
@Service
public class ChatService {

    private final OpenAiChatModel chatModel;
    private static final String OPENAI_URL = "https://api.openai.com/v1/audio/transcriptions";
    private String API_KEY; //TODO remover

    public ChatService(@Value("${openai.api-key}") String apiKey) {
        API_KEY = apiKey;
        this.chatModel = OpenAiChatModel.withApiKey(apiKey);
    }

    public ChatResponse chat(ChatRequest request) {
        log.info("Sending request: " + request);
        var response = chatModel.generate(request.getMessage());
        log.info("Received response: " + response);
        return ChatResponse.builder().message(response).build();
    }

    public VoiceToTextResponse voiceToText(MultipartFile audioFile) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(API_KEY);
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", audioFile.getResource());
            body.add("model", "whisper-1");

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            return restTemplate.exchange(OPENAI_URL, HttpMethod.POST, requestEntity, VoiceToTextResponse.class).getBody();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao transcrever áudio: " + e.getMessage());
        }
    }

}
