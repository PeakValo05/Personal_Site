package com.personal.website.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.personal.website.repository.PersonalRepository;

@Controller
public class PersonalController {

    private final PersonalRepository personalRepository;

    public PersonalController(PersonalRepository personalRepository) {
        this.personalRepository = personalRepository;
    }

    @GetMapping("/personal")
    public String personal(Model model) {
        model.addAttribute("personalModels", personalRepository.findAll());
        return "personal";
    }
}
