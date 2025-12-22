package org.dpnam28.foodcouriers.ui.foodeditor;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.List;

public class FoodEditorContract {
    public interface View {
        void showLoading(boolean loading);
        void onCategoriesLoaded(List<CategoryOption> categories);
        void onFoodLoaded(FoodDetail detail);
        void onSaveSuccess();
        void onError(String message);
    }

    public interface Presenter {
        void loadCategories();
        void loadFoodDetail(long foodId);
        void createFood(Form form, Uri imageUri);
        void updateFood(long foodId, Form form, Uri imageUri);
        void detach();
    }

    public static class CategoryOption {
        private final long id;
        private final String name;

        public CategoryOption(long id, String name) {
            this.id = id;
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }

    public static class FoodDetail {
        private final long id;
        private final String name;
        private final String description;
        private final long categoryId;
        private final double price;
        private final boolean isActive;
        private final String imageUrl;

        public FoodDetail(long id, String name, String description, long categoryId, double price, boolean isActive, String imageUrl) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.categoryId = categoryId;
            this.price = price;
            this.isActive = isActive;
            this.imageUrl = imageUrl;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public long getCategoryId() {
            return categoryId;
        }

        public double getPrice() {
            return price;
        }

        public boolean isActive() {
            return isActive;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    public static class Form {
        private final String name;
        private final String description;
        private final long categoryId;
        private final double price;
        private final boolean isActive;

        public Form(String name, String description, long categoryId, double price, boolean isActive) {
            this.name = name;
            this.description = description;
            this.categoryId = categoryId;
            this.price = price;
            this.isActive = isActive;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public long getCategoryId() {
            return categoryId;
        }

        public double getPrice() {
            return price;
        }

        public boolean isActive() {
            return isActive;
        }
    }
}
