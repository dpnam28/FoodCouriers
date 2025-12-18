package org.dpnam28.foodcouriers.ui.search;

import java.util.List;

public class SearchContract {

    public enum SearchType {
        FOOD,
        RESTAURANT
    }

    public interface View {
        void showLoading(boolean loading);

        void showResults(List<SearchResultItem> results, SearchType type);

        void showEmptyState();

        void showError(String message);
    }

    public interface Presenter {
        void search(String keyword, SearchType type);

        void detach();
    }
}
