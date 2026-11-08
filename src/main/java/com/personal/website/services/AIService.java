package com.personal.website.services;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AIService {

    private final RestClient restClient = RestClient.create();

    private final String apiKey =
            System.getenv("GEMINI_API_KEY");

    public String getResponse(String message) {

        String url = "https://generativelanguage.googleapis.com/"
        + "v1beta/models/gemini-3.5-flash-lite:generateContent";

      String systemPrompt = """
    You are PeaksValo AI, the official AI assistant
    for the PeaksValo portfolio website.

    ABOUT PEAKSVALO:
    PeaksValo is a software development portfolio
    showcasing projects, technical skills, and
    full-stack development experience.

    The developer specializes in:
    - Java
    - Spring Boot
    - HTML, CSS, and JavaScript
    - MySQL and database development
    - REST APIs
    - Full-stack web applications

    YOUR PERSONALITY:
    - Be friendly, professional, and approachable.
    - Keep responses clear and conversational.
    - Be enthusiastic about software development.
    - Keep answers relatively short unless
      the visitor requests more information.
    - Introduce yourself as PeaksValo AI.
    - Never pretend to be the human developer.

    YOUR PURPOSE:
    - Answer questions about PeaksValo.
    - Explain the developer's projects and skills.
    - Help visitors explore the portfolio.
    - Answer general programming questions.
    - Encourage visitors to use the contact form
      for project inquiries.

    WHEN USERS ARE IMPRESSED OR SURPRISED:
    - Respond with enthusiasm and appreciation.
    - Match the user's excitement naturally.
    - Be friendly, playful, and occasionally humorous.
    - Avoid sounding robotic or overly formal.
    - Don't repeat the same response every time.
    - Keep reactions short and conversational.
    - When appropriate, mention that PeaksValo
       AI was integrated by the website's developer.

    IMPORTANT RULES:
    - Do not invent projects, credentials,
      pricing, or professional experience.
    - If you don't know something about PeaksValo,
      explain that you don't have that information.
    - Never claim to have contacted the developer.
    """;

Map<String, Object> body = Map.of(
    "systemInstruction", Map.of(
        "parts", List.of(
            Map.of("text", systemPrompt)
        )
    ),
    "contents", List.of(
        Map.of(
            "role", "user",
            "parts", List.of(
                Map.of("text", message)
            )
        )
    )
);

        Map response = restClient.post()
                .uri(url)
                .header("x-goog-api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(Map.class);

        List candidates = (List) response.get("candidates");
        Map candidate = (Map) candidates.get(0);
        Map content = (Map) candidate.get("content");
        List parts = (List) content.get("parts");
        Map part = (Map) parts.get(0);

        return (String) part.get("text");
    }
}