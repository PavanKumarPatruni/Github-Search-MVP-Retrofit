package com.pavan.githubassignment.home;

import com.pavan.githubassignment.api.models.Item;

import java.util.List;

public interface HomeView {

    void changeMessage(String message);

    void showMessage();

    void hideMessage();

    void updateList(List<Item> items);

    void clearList();

    void showList();

    void hideList();

    void resetFilters();
}
