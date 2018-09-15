package com.pavan.githubassignment.home;

public interface HomePresenter {

    void onSearchClicked(String searchText, String sortBy, String orderBy);

    void onDialogCancel();

    void onFilterApply();

    void onFilterClear();

    void onLoadMore();

}
