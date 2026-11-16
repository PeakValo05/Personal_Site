package com.personal.website.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WorkExperienceController {
    
    @GetMapping("/work-experience")
    public String workExperience() {
        return "work-experience";
    }
}
