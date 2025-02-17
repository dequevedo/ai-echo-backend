package com.pocspringboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocspringboot.configuration.AppConfig;
import com.pocspringboot.model.request.ChatRequest;
import com.pocspringboot.model.request.TextToVoiceRequest;
import com.pocspringboot.model.response.ChatResponse;
import com.pocspringboot.model.response.VoiceToTextResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

@Slf4j
@Service
@AllArgsConstructor
public class ChatService {

    private final OpenAiChatModel chatModel;
    private final RestTemplate restTemplate;
    private final AppConfig appConfig;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatResponse chat(ChatRequest request) {
        try {
            log.info("Sending request: " + request);
            var response = chatModel.generate(request.getMessage());
            log.info("Received response: " + response);
            return ChatResponse.builder().message(response).build();
        } catch (Exception e) {
            throw new RuntimeException("Erro enviar chat: " + e.getMessage());
        }
    }

    public VoiceToTextResponse voiceToText(MultipartFile audioFile) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(appConfig.getOpenAIApiKey());
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", audioFile.getResource());
            body.add("model", "whisper-1");

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            return restTemplate.exchange(appConfig.getSpeechToTextURL(), HttpMethod.POST, requestEntity, VoiceToTextResponse.class).getBody();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao transcrever áudio: " + e.getMessage());
        }
    }

    public ResponseEntity<byte[]> textToVoice(TextToVoiceRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(appConfig.getOpenAIApiKey());
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));

            String requestBody = objectMapper.writeValueAsString(request);

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(appConfig.getTextToSpeechURL(), HttpMethod.POST, requestEntity, byte[].class);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"speech.mp3\"")
                    .body(response.getBody());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter texto em áudio: " + e.getMessage());
        }
    }
}
