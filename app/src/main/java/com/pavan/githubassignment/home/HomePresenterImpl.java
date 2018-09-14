package com.pavan.githubassignment.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.pavan.githubassignment.GitHubApplication;
import com.pavan.githubassignment.R;
import com.pavan.githubassignment.api.models.GetRepos;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenterImpl implements HomePresenter {

    private final HomeView homeView;
    private Context context;

    HomePresenterImpl(Context context) {
        this.context = context;
        this.homeView = (HomeView) context;
    }

    private void onSearching() {
        homeView.changeMessage(context.getString(R.string.fetching));
    }

    private void hideMessage() {
        homeView.hideMessage();
    }

    private void getReposByText(String searchText, String sortBy, String orderBy, int pageLength, final int pageNum) {
        GitHubApplication.getInstance(context).getAPIService().getRepos(searchText, sortBy, orderBy, pageLength, pageNum).enqueue(new Callback<GetRepos>() {
            @Override
            public void onResponse(@NonNull Call<GetRepos> call, Response<GetRepos> response) {
                hideMessage();
                homeView.updateList(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<GetRepos> call, @NonNull Throwable t) {
                homeView.updateList(null);
            }
        });

    }

    @Override
    public void onSearchClicked(String searchText, String sortBy, String orderBy, int pageLength, int pageNum) {
        if (pageNum == 1) {
            onSearching();
        }
        getReposByText(searchText, sortBy, orderBy, pageLength, pageNum);
    }
}
