package com.pavan.githubassignment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pavan.githubassignment.R;
import com.pavan.githubassignment.api.models.Item;
import com.pavan.githubassignment.api.models.Owner;
import com.pavan.githubassignment.home.RepoAdapter;
import com.pavan.githubassignment.repo.RepoActivity;
import com.pavan.githubassignment.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity implements ProfileView, RepoAdapter.OnItemClickListener {

    @BindView(R.id.imageViewProfile)
    ImageView imageViewProfile;

    @BindView(R.id.textViewName)
    TextView textViewName;

    @BindView(R.id.textViewFetching)
    TextView textViewFetching;

    @BindView(R.id.recyclerViewRepos)
    RecyclerView recyclerViewRepos;

    private Owner profile;

    private ProfilePresenterImpl profilePresenterImpl;

    private RepoAdapter repoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        initRecyclerView();

        profilePresenterImpl = new ProfilePresenterImpl(this);

        getIntentData();
    }

    private void initRecyclerView() {
        recyclerViewRepos.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRepos.setLayoutManager(new LinearLayoutManager(this));
        ViewCompat.setNestedScrollingEnabled(recyclerViewRepos, false);

        repoAdapter = new RepoAdapter(false, recyclerViewRepos);
        repoAdapter.setOnItemClickListener(this);
        recyclerViewRepos.setAdapter(repoAdapter);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        profile = intent.getParcelableExtra(Constants.KEY_PROFILE);

        textViewName.setText(profile.getLogin());

        Picasso.get().load(profile.getAvatarUrl())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageViewProfile);

        getRepos();
    }

    private void getRepos() {
        profilePresenterImpl.getRepos(profile.getLogin());
    }

    @Override
    public void attachRepos(List<Item> repoList) {
        repoAdapter.updateList(repoList);

        textViewFetching.setVisibility(View.GONE);
        recyclerViewRepos.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(Item item) {
        launchRepoActivity(item);
    }

    private void launchRepoActivity(Item item) {
        Intent intent = new Intent(this, RepoActivity.class);
        intent.putExtra(Constants.KEY_REPO, item);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
