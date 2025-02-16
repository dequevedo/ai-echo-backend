package com.pocspringboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocspringboot.model.request.ChatRequest;
import com.pocspringboot.model.request.TextToVoiceRequest;
import com.pocspringboot.model.response.ChatResponse;
import com.pocspringboot.model.response.VoiceToTextResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
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
public class ChatService {

    private final OpenAiChatModel chatModel;
    private static final String SPEECH_TO_TEXT_URL = "https://api.openai.com/v1/audio/transcriptions";
    private static final String TEXT_TO_SPEECH_URL = "https://api.openai.com/v1/audio/speech";
    private String API_KEY; //TODO remover

    private final ObjectMapper objectMapper = new ObjectMapper(); // Para converter o objeto para JSON


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

            return restTemplate.exchange(SPEECH_TO_TEXT_URL, HttpMethod.POST, requestEntity, VoiceToTextResponse.class).getBody();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao transcrever áudio: " + e.getMessage());
        }
    }

    public ResponseEntity<byte[]> textToVoice(TextToVoiceRequest request) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(API_KEY);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM)); // Para receber o arquivo de áudio

            // ✅ Serializa manualmente o objeto para JSON
            String requestBody = objectMapper.writeValueAsString(request);

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            // ✅ Envia a requisição e recebe o áudio como array de bytes
            ResponseEntity<byte[]> response = restTemplate.exchange(TEXT_TO_SPEECH_URL, HttpMethod.POST, requestEntity, byte[].class);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"speech.mp3\"") // Nome do arquivo
                    .body(response.getBody());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter texto em áudio: " + e.getMessage());
        }
    }
}
