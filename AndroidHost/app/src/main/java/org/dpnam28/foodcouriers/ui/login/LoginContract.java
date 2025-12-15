package org.dpnam28.foodcouriers.ui.login;

public class LoginContract {

    private LoginContract() {
    }

    public interface View {
        void showLoading(boolean isLoading);
        void onRegisterSuccess();
        void onRegisterError(String message);
    }

    public interface Presenter {
        void register(UserForm form);
        void detach();
    }

    public static class UserForm {
        private final String fullName;
        private final String email;
        private final String password;
        private final String phoneNumber;
        private final String address;
        private final String role;
        private final long locationId;

        public UserForm(
                String fullName,
                String email,
                String password,
                String phoneNumber,
                String address,
                String role,
                long locationId
        ) {
            this.fullName = fullName;
            this.email = email;
            this.password = password;
            this.phoneNumber = phoneNumber;
            this.address = address;
            this.role = role;
            this.locationId = locationId;
        }

        public String getFullName() {
            return fullName;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
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
    }
}
