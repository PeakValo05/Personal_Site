package com.personal.website.controllers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;

import com.personal.website.services.ContactService;

@Controller
public class PricingController {

    private static final Logger logger = LoggerFactory.getLogger(PricingController.class);
    private final ContactService contactService;

    public PricingController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/pricing")
    public String getPricingPage() {
        return "pricing";
    }

    @PostMapping("/checkout/order")
    @ResponseBody
    public ResponseEntity<Map<String, String>> submitOrder(@RequestBody(required = false) OrderRequest order) {
        if (order == null || !isValidName(order.name()) || !isValidEmail(order.email())) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Enter a valid name and email address."));
        }

        if (!isValidDescription(order.message())) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Add a short description of your project (up to 1,000 characters)."));
        }

        PackageDetails packageDetails = getPackageDetails(order.packageName());
        if (packageDetails == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Choose a valid pricing package before submitting."));
        }

        try {
            contactService.sendOrderRequest(
                order.name().trim(),
                order.email().trim(),
                packageDetails.name(),
                packageDetails.price(),
                order.message().trim()
            );
            return ResponseEntity.ok(Map.of("message", "Your order request was sent. I’ll be in touch soon."));
        } catch (RestClientException | IllegalStateException exception) {
            logger.error("Failed to send pricing order through Resend", exception);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(Map.of("message", "Your order could not be sent right now. Please try again later."));
        }
    }

    private boolean isValidName(String name) {
        return name != null && !name.isBlank() && name.trim().length() <= 120;
    }

    private boolean isValidDescription(String message) {
        return message != null && !message.isBlank() && message.trim().length() <= 1000;
    }

    private boolean isValidEmail(String email) {
        return email != null
            && email.length() <= 254
            && email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }

    private PackageDetails getPackageDetails(String packageName) {
        if (packageName == null) {
            return null;
        }

        return switch (packageName) {
            case "bronze" -> new PackageDetails("Bronze", 350);
            case "silver" -> new PackageDetails("Silver", 750);
            case "gold" -> new PackageDetails("Gold", 1500);
            default -> null;
        };
    }

    public record OrderRequest(String name, String email, String packageName, String message) {
    }

    private record PackageDetails(String name, int price) {
    }
}
