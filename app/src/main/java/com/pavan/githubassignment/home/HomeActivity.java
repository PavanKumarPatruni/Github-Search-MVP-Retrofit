package com.pavan.githubassignment.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pavan.githubassignment.R;
import com.pavan.githubassignment.api.models.Item;
import com.pavan.githubassignment.repo.RepoActivity;
import com.pavan.githubassignment.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity implements HomeView, RepoAdapter.OnItemClickListener, View.OnClickListener, RepoAdapter.OnLoadMoreListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.editTextSearch)
    EditText editTextSearch;

    @BindView(R.id.textViewMessage)
    TextView textViewMessage;

    TextView textViewStars;
    TextView textViewForks;
    TextView textViewUpdated;
    TextView textViewAscending;
    TextView textViewDescending;
    TextView textViewApply;
    TextView textViewClear;

    private HomePresenterImpl homePresenterImpl;

    private RepoAdapter repoAdapter;

    private String orderBy, sortBy;

    private BottomSheetDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        homePresenterImpl = new HomePresenterImpl(this);

        initRecyclerView();
        initBottomSheet();

        editTextSearch.setText("android");
        searchClick();
    }

    private void initRecyclerView() {
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        repoAdapter = new RepoAdapter(true, recyclerView);
        repoAdapter.setOnItemClickListener(this);
        repoAdapter.setOnLoadMoreListener(this);
        recyclerView.setAdapter(repoAdapter);
    }

    private void initBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_filter, null);

        textViewStars = view.findViewById(R.id.textViewStars);
        textViewForks = view.findViewById(R.id.textViewForks);
        textViewUpdated = view.findViewById(R.id.textViewUpdated);
        textViewAscending = view.findViewById(R.id.textViewAscending);
        textViewDescending = view.findViewById(R.id.textViewDescending);
        textViewApply = view.findViewById(R.id.textViewApply);
        textViewClear = view.findViewById(R.id.textViewClear);

        textViewStars.setOnClickListener(this);
        textViewForks.setOnClickListener(this);
        textViewUpdated.setOnClickListener(this);
        textViewAscending.setOnClickListener(this);
        textViewDescending.setOnClickListener(this);
        textViewApply.setOnClickListener(this);
        textViewClear.setOnClickListener(this);

        dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                homePresenterImpl.onDialogCancel();
            }
        });
    }

    @OnClick(R.id.imageViewSearch)
    public void searchClick() {
        homePresenterImpl.onSearchClicked(editTextSearch.getText().toString(), sortBy, orderBy);
    }

    @OnClick(R.id.imageViewFilters)
    public void filterClick() {
        if (editTextSearch.getText().toString().length() != 0) {
            dialog.show();
        }
    }

    @Override
    public void changeMessage(String message) {
        showMessage();
        textViewMessage.setText(message);
    }

    @Override
    public void showMessage() {
        hideList();
        textViewMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideMessage() {
        textViewMessage.setVisibility(View.GONE);
        showList();
    }

    @Override
    public void showList() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideList() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void resetFilters() {
        clearSortButtons();
        clearOrderButtons();
    }

    @Override
    public void updateList(List<Item> items) {
        repoAdapter.setLoaded();
        repoAdapter.updateList(items);
    }

    @Override
    public void clearList() {
        repoAdapter.clearList();
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

    private void manageSortButtons() {
        clearSortButtons();

        switch (sortBy) {
            case "stars":
                textViewStars.setSelected(true);
                break;
            case "forks":
                textViewForks.setSelected(true);
                break;
            case "updated":
                textViewUpdated.setSelected(true);
                break;
        }
    }

    private void clearSortButtons() {
        textViewStars.setSelected(false);
        textViewForks.setSelected(false);
        textViewUpdated.setSelected(false);
    }

    private void manageOrderButtons() {
        clearOrderButtons();

        switch (orderBy) {
            case "desc":
                textViewDescending.setSelected(true);
                break;
            case "asc":
                textViewAscending.setSelected(true);
                break;
        }
    }

    private void clearOrderButtons() {
        textViewDescending.setSelected(false);
        textViewAscending.setSelected(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewStars:
                sortBy = "stars";
                manageSortButtons();
                break;
            case R.id.textViewForks:
                sortBy = "forks";
                manageSortButtons();
                break;
            case R.id.textViewUpdated:
                sortBy = "updated";
                manageSortButtons();
                break;
            case R.id.textViewDescending:
                orderBy = "desc";
                manageOrderButtons();
                break;
            case R.id.textViewAscending:
                orderBy = "asc";
                manageOrderButtons();
                break;
            case R.id.textViewApply:
                dialog.dismiss();
                homePresenterImpl.onFilterApply();
                searchClick();
                break;
            case R.id.textViewClear:
                dialog.dismiss();
                homePresenterImpl.onFilterClear();
                break;
        }
    }

    @Override
    public void onLoadMore() {
        homePresenterImpl.onLoadMore();
    }
}
