package com.pocspringboot.controller;

import com.pocspringboot.model.request.ChatRequest;
import com.pocspringboot.model.response.ChatResponse;
import com.pocspringboot.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class ChatController {

    private final ChatService service;

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return service.chat(request);
    }
}