package com.pavan.githubassignment.home;

public interface HomePresenter {

    void onSearchClicked(String searchText, String sortBy, String orderBy, int pageLength, int pageNum);

}
