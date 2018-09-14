package com.pavan.githubassignment;

import android.app.Application;
import android.content.Context;

import com.pavan.githubassignment.api.APIConstants;
import com.pavan.githubassignment.api.APIService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GitHubApplication extends Application {

    private APIService service;

    public static GitHubApplication getInstance(Context context) {
        return (GitHubApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIConstants.HOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(APIService.class);

    }

    public APIService getAPIService() {
        return service;
    }

}
