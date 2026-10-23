package com.personal.website.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ContactService {

    private final RestClient restClient = RestClient.create("https://api.resend.com");
    private final String apiKey;
    private final String fromEmail;
    private final String toEmail;

    public ContactService(
            @Value("${resend.api-key}") String apiKey,
            @Value("${resend.from-email}") String fromEmail,
            @Value("${resend.to-email}") String toEmail) {
        this.apiKey = apiKey;
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
    }

    public void sendContactMessage(
            String name,
            String email,
            
            String message) {

        if (apiKey.isBlank()) {
            throw new IllegalStateException("Resend API key is not configured");
        }

        Map<String, Object> payload = Map.of(
            "from", fromEmail,
            "to", List.of(toEmail),
            "subject", "Portfolio Contact - " + "Jackson Wiser",
            "reply_to", email,
            "text", "Name: " + name + "\nEmail: " + email + "\n\nMessage:\n" + message
        );

        sendEmail(payload);
    }

    public void sendOrderRequest(String name, String email, String packageName, int packagePrice, String message) {
        if (apiKey.isBlank()) {
            throw new IllegalStateException("Resend API key is not configured");
        }

        Map<String, Object> payload = Map.of(
            "from", fromEmail,
            "to", List.of(toEmail),
            "subject", "New Website Order Request - " + packageName,
            "reply_to", email,
            "text", "A new order request was submitted.\n\n"
                + "Name: " + name + "\n"
                + "Email: " + email + "\n"
                + "Package: " + packageName + "\n"
                + "Starting price: $" + packagePrice + "\n"
                + "Description: " + message + "\n"
        );

        sendEmail(payload);
    }

    private void sendEmail(Map<String, Object> payload) {
        restClient.post()
            .uri("/emails")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
            .contentType(MediaType.APPLICATION_JSON)
            .body(payload)
            .retrieve()
            .toBodilessEntity();
    }
}