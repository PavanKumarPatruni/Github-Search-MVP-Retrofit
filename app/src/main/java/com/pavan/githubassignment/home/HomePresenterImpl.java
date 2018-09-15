package com.pavan.githubassignment.home;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.pavan.githubassignment.GitHubApplication;
import com.pavan.githubassignment.R;
import com.pavan.githubassignment.api.models.GetRepos;
import com.pavan.githubassignment.utils.KeyboardUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenterImpl implements HomePresenter {

    private final HomeView homeView;
    private Context context;

    private int pageLength = 10, pageNum = 1, totalCount = 0, itemsCount = 0;
    private boolean isFilterApplied;

    private String searchText = "", sortBy = "", orderBy = "";

    HomePresenterImpl(Context context) {
        this.context = context;
        this.homeView = (HomeView) context;
    }

    private void onSearching() {
        homeView.changeMessage(context.getString(R.string.fetching));
    }

    private void changeMessage(String message) {
        homeView.changeMessage(message);
    }

    private void hideMessage() {
        homeView.hideMessage();
    }

    private void getReposByText() {
        GitHubApplication.getInstance(context).getAPIService().getRepos(searchText, sortBy, orderBy, pageLength, pageNum).enqueue(new Callback<GetRepos>() {
            @Override
            public void onResponse(@NonNull Call<GetRepos> call, @NonNull Response<GetRepos> response) {
                hideMessage();

                GetRepos getRepos = response.body();
                if (getRepos != null) {
                    if (getRepos.getItems() != null) {
                        totalCount = getRepos.getTotalCount();
                        itemsCount += getRepos.getItems().size();

                        showNoRepoFound();

                        if (itemsCount != 0) {
                            homeView.updateList(getRepos.getItems());
                        }
                    } else {
                        showNoRepoFound();
                    }
                } else {
                    showNoRepoFound();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetRepos> call, @NonNull Throwable t) {
                showNoRepoFound();
            }
        });

    }

    private void showNoRepoFound() {
        if (itemsCount == 0) {
            changeMessage(context.getString(R.string.no_repo_found));
        }
    }

    @Override
    public void onSearchClicked(String searchText, String sortBy, String orderBy) {
        KeyboardUtils.hideKeyboard((Activity) context);

        if (!this.searchText.equalsIgnoreCase(searchText)) {
            resetData();
        }

        this.searchText = searchText;
        this.sortBy = sortBy;
        this.orderBy = orderBy;

        if (pageNum == 1) {
            onSearching();
        }
        getReposByText();
    }

    @Override
    public void onDialogCancel() {
        if (isFilterApplied) {
            getReposByText();
        } else {
            clearFilters();
        }
    }

    @Override
    public void onFilterApply() {
        isFilterApplied = true;
        resetData();
        getReposByText();
    }

    @Override
    public void onFilterClear() {
        clearFilters();
        resetData();
        if (isFilterApplied) {
            isFilterApplied = false;
            getReposByText();
        }
    }

    @Override
    public void onLoadMore() {
        if (itemsCount != 0 && totalCount != itemsCount) {
            pageNum++;
            getReposByText();
        }
    }

    private void resetData() {
        homeView.clearList();

        pageLength = 10;
        pageNum = 1;

        totalCount = itemsCount = 0;
    }

    private void clearFilters() {
        orderBy = sortBy = "";
        homeView.resetFilters();
    }

}
