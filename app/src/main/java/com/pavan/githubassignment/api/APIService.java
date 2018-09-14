package com.pavan.githubassignment.api;

import com.pavan.githubassignment.api.models.GetRepos;
import com.pavan.githubassignment.api.models.Item;
import com.pavan.githubassignment.api.models.Owner;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @GET(APIConstants.GET_REPOS)
    Call<GetRepos> getRepos(@Query("q") String q, @Query("sort") String sort, @Query("order") String order, @Query("per_page") int per_page, @Query("page") int page);

    @GET(APIConstants.GET_CONTRIBUTORS)
    Call<List<Owner>> getContributors(@Path("ownerName") String ownerName, @Path("repoName") String repoName);

    @GET(APIConstants.GET_REPOS_BY_NAME)
    Call<List<Item>> getReposByName(@Path("ownerName") String ownerName);

}
