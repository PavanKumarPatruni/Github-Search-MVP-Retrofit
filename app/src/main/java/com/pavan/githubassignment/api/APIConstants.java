package com.pavan.githubassignment.api;

public class APIConstants {

    public static final String HOST_URL = "https://api.github.com";

    public static final String GET_REPOS = "/search/repositories";

    public static final String GET_CONTRIBUTORS = "/repos/{ownerName}/{repoName}/contributors";

    public static final String GET_REPOS_BY_NAME = "/users/{ownerName}/repos";
}
