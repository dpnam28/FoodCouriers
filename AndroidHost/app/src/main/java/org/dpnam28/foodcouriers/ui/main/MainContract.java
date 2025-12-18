package org.dpnam28.foodcouriers.ui.main;

import java.util.List;

public class MainContract {

    public interface View {
        void showCategories(List<CategoryItem> categories, long selectedCategoryId);

        void showFoods(List<FoodItem> foods);

        void showFoodsLoading(boolean loading);

        void showEmptyFoods(boolean isEmpty);

        void showError(String message);
    }

    public interface Presenter {
        void loadInitialData();

        void selectCategory(long categoryId);

        void detach();
    }
}
