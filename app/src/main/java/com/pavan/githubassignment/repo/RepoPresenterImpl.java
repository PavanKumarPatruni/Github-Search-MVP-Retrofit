package com.pavan.githubassignment.repo;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pavan.githubassignment.GitHubApplication;
import com.pavan.githubassignment.R;
import com.pavan.githubassignment.api.models.Owner;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoPresenterImpl implements RepoPresenter {

    private final Context context;
    private final RepoView repoView;

    RepoPresenterImpl(Context context) {
        this.context = context;
        this.repoView = (RepoView) context;
    }

    @Override
    public void getContributors(String ownerName, final String repoName) {

        GitHubApplication.getInstance(context).getAPIService().getContributors(ownerName, repoName).enqueue(new Callback<List<Owner>>() {
            @Override
            public void onResponse(@NonNull Call<List<Owner>> call, @NonNull Response<List<Owner>> response) {
                if (response.body() != null) {
                    repoView.attachContributors(response.body());
                } else {
                    repoView.changeMessage(context.getResources().getString(R.string.no_contributors));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Owner>> call, @NonNull Throwable t) {
                repoView.changeMessage(context.getResources().getString(R.string.no_contributors));
            }
        });

    }
}
