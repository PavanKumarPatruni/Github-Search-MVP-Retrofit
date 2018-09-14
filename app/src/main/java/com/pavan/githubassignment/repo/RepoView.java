package com.pavan.githubassignment.repo;

import com.pavan.githubassignment.api.models.Owner;

import java.util.List;

public interface RepoView {

    void attachContributors(List<Owner> contributorList);

}
