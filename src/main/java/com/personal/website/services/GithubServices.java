package com.personal.website.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.personal.website.models.GithubModel;
import com.personal.website.models.GithubRepoModel;

@Service 
public class GithubServices {

    private final String GITHUB_URL = "https://api.github.com/users/PeakValo05";

    private final String GITHUB_REPOS_URL = "https://api.github.com/repos/PeakValo05";

    private final RestTemplate restTemplate = new RestTemplate();


    public GithubModel getGithubUser() {

        return restTemplate.getForObject(
            GITHUB_URL, 
            GithubModel.class
        );
    }

    // Fetches the list of GitHub repositories for the user
    public List<GithubRepoModel> getGithubRepos() {
        GithubRepoModel[] repos = restTemplate.getForObject(
            GITHUB_URL + "/repos",
            GithubRepoModel[].class
        );

        if (repos == null) {
            return List.of();
        }

        return Arrays.asList(repos);
    }

    public int getTotalCommitCount(String repoName) {

        if (repoName == null || repoName.isBlank()) {
            return 0;
        }

        String url = GITHUB_REPOS_URL
                + "/" + repoName
                + "/commits?per_page=100";

        Object[] commits = restTemplate.getForObject(
                url,
                Object[].class
        );

        return commits == null ? 0 : commits.length;
    }

    public int getTotalCommitCount() {

        List<GithubRepoModel> repos = getGithubRepos();

        if (repos == null || repos.isEmpty()) {
            return 0;
        }

        int totalCommits = 0;

        for (GithubRepoModel repo : repos) {
            if (repo != null && repo.getName() != null) {
                totalCommits += getTotalCommitCount(repo.getName());
            }
        }

        return totalCommits;
    }
}