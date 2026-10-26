package com.personal.website.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClickerGame {

    @GetMapping("/clicker")
    public String clicker() {
        return "clickergame";
    }
}