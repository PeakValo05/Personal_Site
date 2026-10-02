package com.personal.website.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestClientException;
import com.personal.website.models.GithubRepoModel;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.personal.website.models.ContactModel;
import com.personal.website.services.GithubServices;

@Controller
public class HomeControllers {

    GithubServices githubServices = new GithubServices();

    @GetMapping("/")
    public String home(Model model) {

        List<GithubRepoModel> repos = List.of();
        try {
            repos = githubServices.getGithubRepos();
        } catch (RestClientException e) {
            System.err.println("Could not load GitHub repos: " + e.getMessage());
        }
        model.addAttribute("repoCount", repos.size());
        model.addAttribute("repos", repos);

        int commitCount = 0;
        try {
            commitCount = githubServices.getTotalCommitCount("Personal_Site");
        } catch (RestClientException e) {
            System.err.println("Could not load commit count: " + e.getMessage());
        }
        model.addAttribute("commitCount", commitCount);

        model.addAttribute("contact", new ContactModel());

        return "index";
    }
}