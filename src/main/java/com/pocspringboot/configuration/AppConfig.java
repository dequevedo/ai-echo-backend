package com.pocspringboot.configuration;

import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import org.springframework.web.client.RestTemplate;

@Configuration
@Getter
public class AppConfig {

    @Value("${openai.api-key}")
    private String openAIApiKey;

    private final String speechToTextURL = "https://api.openai.com/v1/audio/transcriptions";

    private final String textToSpeechURL = "https://api.openai.com/v1/audio/speech";

    @Bean
    public OpenAiChatModel openAiChatModel() {
        return OpenAiChatModel.withApiKey(openAIApiKey);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}

