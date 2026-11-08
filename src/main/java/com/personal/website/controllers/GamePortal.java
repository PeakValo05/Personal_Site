package com.personal.website.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GamePortal {


    @GetMapping({"/games", "/game-portal"})
    public String gamePortal() {
        return "gameportal";
    }
    
}
