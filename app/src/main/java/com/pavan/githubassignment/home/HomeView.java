package com.pavan.githubassignment.home;


import com.pavan.githubassignment.api.models.GetRepos;

public interface HomeView {

    void changeMessage(String message);

    void showMessage();

    void hideMessage();

    void updateList(GetRepos getRepos);

    void showList();

    void hideList();
}
