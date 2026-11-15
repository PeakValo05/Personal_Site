package com.personal.website.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PersonalController {

    @GetMapping("/personal")
    public String getPersonalPage(Model model) {
        // Add your personal models to the model here
        // Example: model.addAttribute("personalModels", personalModels);
        return "personal";
    }
}
