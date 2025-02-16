package com.pocspringboot.controller;

import com.pocspringboot.model.request.ChatRequest;
import com.pocspringboot.model.request.TextToVoiceRequest;
import com.pocspringboot.model.response.ChatResponse;
import com.pocspringboot.model.response.VoiceToTextResponse;
import com.pocspringboot.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
public class ChatController {

    private final ChatService service;

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return service.chat(request);
    }

    @PostMapping("/voice-to-text")
    public VoiceToTextResponse voiceToText(@RequestParam("file") MultipartFile file) {
        return service.voiceToText(file);
    }

    @PostMapping("/text-to-voice")
    public ResponseEntity<byte[]> textToVoice(@RequestBody TextToVoiceRequest request) {
        return service.textToVoice(request);
    }
}