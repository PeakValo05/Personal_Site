package com.personal.website.services;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.personal.website.models.GithubModel;
import com.personal.website.models.GithubRepoModel;

@Service 
public class GithubServices {

    private final String GITHUB_URL = "https://api.github.com/users/PeakValo05";

    private final String GITHUB_REPOS_URL = "https://api.github.com/repos/PeakValo05";

    private static final long CACHE_TTL_MS = 10 * 60 * 1000;

    private final RestTemplate restTemplate = new RestTemplate();

    private List<GithubRepoModel> cachedRepos = List.of();
    private long reposFetchedAt = 0;
    private final Map<String, Integer> cachedCommitCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> commitCountsFetchedAt = new ConcurrentHashMap<>();

    public GithubServices() {
        // Optional token raises the GitHub rate limit from 60 to 5000 requests/hour
        String token = System.getenv("GITHUB_TOKEN");
        if (token != null && !token.isBlank()) {
            restTemplate.getInterceptors().add((request, body, execution) -> {
                request.getHeaders().setBearerAuth(token);
                return execution.execute(request, body);
            });
        }
    }

    public GithubModel getGithubUser() {

        return restTemplate.getForObject(
            GITHUB_URL, 
            GithubModel.class
        );
    }

    // Fetches the list of GitHub repositories for the user
    public synchronized List<GithubRepoModel> getGithubRepos() {
        if (System.currentTimeMillis() - reposFetchedAt < CACHE_TTL_MS) {
            return cachedRepos;
        }

        try {
            GithubRepoModel[] repos = restTemplate.getForObject(
                GITHUB_URL + "/repos?per_page=100",
                GithubRepoModel[].class
            );
            cachedRepos = repos == null ? List.of() : Arrays.asList(repos);
        } catch (RestClientException e) {
            System.err.println("Could not load GitHub repos: " + e.getMessage());
        }
        // Mark as fetched even on failure so we don't hammer a rate-limited API
        reposFetchedAt = System.currentTimeMillis();
        return cachedRepos;
    }

    public int getTotalCommitCount(String repoName) {

        if (repoName == null || repoName.isBlank()) {
            return 0;
        }

        long fetchedAt = commitCountsFetchedAt.getOrDefault(repoName, 0L);
        if (System.currentTimeMillis() - fetchedAt < CACHE_TTL_MS) {
            return cachedCommitCounts.getOrDefault(repoName, 0);
        }

        String url = GITHUB_REPOS_URL
                + "/" + repoName
                + "/commits?per_page=100";

        try {
            Object[] commits = restTemplate.getForObject(
                    url,
                    Object[].class
            );
            cachedCommitCounts.put(repoName, commits == null ? 0 : commits.length);
        } catch (RestClientException e) {
            System.err.println("Could not load commit count: " + e.getMessage());
        }
        commitCountsFetchedAt.put(repoName, System.currentTimeMillis());
        return cachedCommitCounts.getOrDefault(repoName, 0);
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