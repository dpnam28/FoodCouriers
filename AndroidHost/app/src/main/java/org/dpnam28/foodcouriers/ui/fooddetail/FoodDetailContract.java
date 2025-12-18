package org.dpnam28.foodcouriers.ui.fooddetail;

public class FoodDetailContract {

    interface View {
        void showLoading(boolean loading);

        void showFood(FoodDetail detail);

        void showError(String message);
    }

    interface Presenter {
        void loadFood(long foodId);

        void detach();
    }

    public static class FoodDetail {
        private final String name;
        private final String description;
        private final double price;
        private final String imageUrl;

        public FoodDetail(String name, String description, double price, String imageUrl) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public double getPrice() {
            return price;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}
