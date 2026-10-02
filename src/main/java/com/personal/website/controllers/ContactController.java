package com.personal.website.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.personal.website.models.ContactModel;
import com.personal.website.services.ContactService;

@Controller
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contact")
    public String showContactForm(Model model) {
        model.addAttribute("contact", new ContactModel());
        return "contact";
    }

    @PostMapping("/contact")
    public String submitContact(ContactModel contact, RedirectAttributes redirectAttributes) {
        try {
            contactService.sendContactMessage(
                contact.getFirstName() + " " + contact.getLastName(),
                contact.getEmail(),
                contact.getMessage()
            );
            redirectAttributes.addFlashAttribute("contactSuccess", "Your message was sent successfully.");
        } catch (RestClientException | IllegalStateException exception) {
            logger.error("Failed to send contact form through Resend", exception);
            redirectAttributes.addFlashAttribute(
                "contactError",
                "Your message could not be sent. Please try again later."
            );
        }

        return "redirect:/contact";
    }
}
