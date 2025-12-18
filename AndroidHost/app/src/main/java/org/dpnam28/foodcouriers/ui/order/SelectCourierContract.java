package org.dpnam28.foodcouriers.ui.order;

import java.util.List;

class SelectCourierContract {

    interface View {
        void showLoading(boolean loading);

        void showCouriers(List<CourierItem> couriers);

        void showError(String message);

        void onAssignmentSuccess(String message);
    }

    interface Presenter {
        void loadCouriers(String location);

        void assignCourier(long orderId, long restaurantId, long courierId);

        void detach();
    }

    public static class CourierItem {
        private final long id;
        private final String fullName;
        private final String phoneNumber;
        private final String location;

        CourierItem(long id, String fullName, String phoneNumber, String location) {
            this.id = id;
            this.fullName = fullName;
            this.phoneNumber = phoneNumber;
            this.location = location;
        }

        public long getId() {
            return id;
        }

        public String getFullName() {
            return fullName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getLocation() {
            return location;
        }
    }
}
