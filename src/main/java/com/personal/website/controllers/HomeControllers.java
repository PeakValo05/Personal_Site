package com.personal.website.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.personal.website.models.ContactModel;
import com.personal.website.services.GithubServices;

@Controller
public class HomeControllers {

    GithubServices githubServices = new GithubServices();

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("repoCount", githubServices.getGithubRepos().size());
        model.addAttribute("repos", githubServices.getGithubRepos());

        int commitCount = githubServices.getTotalCommitCount("Personal_Site");
        System.out.println("COMMIT COUNT = " + commitCount);
        model.addAttribute("commitCount", commitCount);

        model.addAttribute("contact", new ContactModel());

        return "index";
    }
}