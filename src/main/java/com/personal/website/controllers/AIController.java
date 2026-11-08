package com.personal.website.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personal.website.services.AIService;

@RestController
@RequestMapping("/api/ai") 
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/chat")
    public String handleAIRequest(@RequestBody String message) {
        System.out.println("User said: " + message);
        // Implement your AI handling logic here
        return aiService.getResponse(message);
    }
}
