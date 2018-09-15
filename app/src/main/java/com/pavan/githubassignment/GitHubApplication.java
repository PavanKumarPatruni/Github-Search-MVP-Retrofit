package com.pavan.githubassignment;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.pavan.githubassignment.api.APIConstants;
import com.pavan.githubassignment.api.APIService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class GitHubApplication extends Application {

    private APIService service;

    public static GitHubApplication getInstance(Context context) {
        return (GitHubApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                Timber.i(message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIConstants.HOST_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(APIService.class);

    }

    public APIService getAPIService() {
        return service;
    }

}
