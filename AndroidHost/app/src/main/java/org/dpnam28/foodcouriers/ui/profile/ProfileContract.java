package org.dpnam28.foodcouriers.ui.profile;

import android.net.Uri;

public class ProfileContract {

    private ProfileContract() {
    }

    public interface View {
        void showLoading(boolean isLoading);

        void onUserLoaded(UserDetail detail);

        void onUpdateSuccess(UserDetail detail);

        void onError(String message);
    }

    public interface Presenter {
        void getUser(long userId);

        void updateUser(UpdateForm form, Uri bannerImageUri);

        void detach();
    }

    public static class UserDetail {
        private final long id;
        private final String email;
        private final String fullName;
        private final String phoneNumber;
        private final String address;
        private final String role;
        private final long locationId;
        private final String locationName;
        private final String description;
        private final String bannerImage;

        public UserDetail(long id,
                          String email,
                          String fullName,
                          String phoneNumber,
                          String address,
                          String role,
                          long locationId,
                          String locationName,
                          String description,
                          String bannerImage) {
            this.id = id;
            this.email = email;
            this.fullName = fullName;
            this.phoneNumber = phoneNumber;
            this.address = address;
            this.role = role;
            this.locationId = locationId;
            this.locationName = locationName;
            this.description = description;
            this.bannerImage = bannerImage;
        }

        public long getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getFullName() {
            return fullName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getAddress() {
            return address;
        }

        public String getRole() {
            return role;
        }

        public long getLocationId() {
            return locationId;
        }

        public String getLocationName() {
            return locationName;
        }

        public String getDescription() {
            return description;
        }

        public String getBannerImage() {
            return bannerImage;
        }

    }

    public static class UpdateForm {
        private final long userId;
        private final String password;
        private final String fullName;
        private final String phoneNumber;
        private final String address;
        private final boolean restaurant;
        private final String description;

        public UpdateForm(long userId,
                          String password,
                          String fullName,
                          String phoneNumber,
                          String address,
                          boolean restaurant,
                          String description) {
            this.userId = userId;
            this.password = password;
            this.fullName = fullName;
            this.phoneNumber = phoneNumber;
            this.address = address;
            this.restaurant = restaurant;
            this.description = description;
        }

        public long getUserId() {
            return userId;
        }

        public String getPassword() {
            return password;
        }

        public String getFullName() {
            return fullName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getAddress() {
            return address;
        }

        public boolean isRestaurant() {
            return restaurant;
        }

        public String getDescription() {
            return description;
        }

    }
}
