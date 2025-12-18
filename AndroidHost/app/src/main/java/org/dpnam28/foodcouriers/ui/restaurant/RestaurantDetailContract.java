package org.dpnam28.foodcouriers.ui.restaurant;

import org.dpnam28.foodcouriers.ui.menu.RestaurantMenuItem;

import java.util.List;

public class RestaurantDetailContract {

    public interface View {
        void showLoading(boolean loading);

        void showRestaurantInfo(RestaurantInfo info);

        void showFoods(List<RestaurantMenuItem> foods);

        void showNoFoodsMessage(boolean show);

        void showError(String message);
    }

    public interface Presenter {
        void loadRestaurant(long id);

        void detach();
    }

    public static class RestaurantInfo {
        private final String name;
        private final String description;
        private final String address;
        private final String phone;
        private final String location;
        private final String deliveryFee;
        private final String imageUrl;

        public RestaurantInfo(String name, String description, String address, String phone, String location, String deliveryFee, String imageUrl) {
            this.name = name;
            this.description = description;
            this.address = address;
            this.phone = phone;
            this.location = location;
            this.deliveryFee = deliveryFee;
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getAddress() {
            return address;
        }

        public String getPhone() {
            return phone;
        }

        public String getLocation() {
            return location;
        }

        public String getDeliveryFee() {
            return deliveryFee;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}
