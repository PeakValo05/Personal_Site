package com.personal.website.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GamePortal {


    @RequestMapping("/game-portal")
    public String gamePortal() {
        return "gameportal";
    }
    
}
