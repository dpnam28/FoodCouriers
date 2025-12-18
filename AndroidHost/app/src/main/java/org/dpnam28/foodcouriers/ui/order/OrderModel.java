package org.dpnam28.foodcouriers.ui.order;

import java.util.List;

public class OrderModel {
    private final long id;
    private final String status;
    private final String statusLabel;
    private final double totalPrice;
    private final RestaurantInfo restaurant;
    private final CustomerInfo customer;
    private final CourierInfo courier;
    private final List<OrderItem> items;

     OrderModel(long id,
               String status,
               String statusLabel,
               double totalPrice,
               RestaurantInfo restaurant,
               CustomerInfo customer,
               CourierInfo courier,
               List<OrderItem> items) {
        this.id = id;
        this.status = status;
        this.statusLabel = statusLabel;
        this.totalPrice = totalPrice;
        this.restaurant = restaurant;
        this.customer = customer;
        this.courier = courier;
        this.items = items;
    }

    public long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public RestaurantInfo getRestaurant() {
        return restaurant;
    }

    public CustomerInfo getCustomer() {
        return customer;
    }

    public CourierInfo getCourier() {
        return courier;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public boolean canCancel() {
        return !"DELIVERED".equals(status) && !"CANCELED".equals(status);
    }

    public boolean canAccept() {
        return "PENDING".equals(status);
    }

    public static class RestaurantInfo {
        private final long id;
        private final String name;
        private final String address;
        private final String phoneNumber;

        RestaurantInfo(long id, String name, String address, String phoneNumber) {
            this.id = id;
            this.name = name;
            this.address = address;
            this.phoneNumber = phoneNumber;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }
    }

    public static class CustomerInfo {
        private final long id;
        private final String fullName;
        private final String address;
        private final String phoneNumber;

        CustomerInfo(long id, String fullName, String address, String phoneNumber) {
            this.id = id;
            this.fullName = fullName;
            this.address = address;
            this.phoneNumber = phoneNumber;
        }

        public long getId() {
            return id;
        }

        public String getFullName() {
            return fullName;
        }

        public String getAddress() {
            return address;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }
    }

    public static class CourierInfo {
        private final long id;
        private final String fullName;
        private final String phoneNumber;
        private final boolean available;

        CourierInfo(long id, String fullName, String phoneNumber, boolean available) {
            this.id = id;
            this.fullName = fullName;
            this.phoneNumber = phoneNumber;
            this.available = available;
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

        public boolean isAvailable() {
            return available;
        }
    }

    public static class OrderItem {
        private final long id;
        private final long foodId;
        private final String foodName;
        private final int quantity;

        OrderItem(long id, long foodId, String foodName, int quantity) {
            this.id = id;
            this.foodId = foodId;
            this.foodName = foodName;
            this.quantity = quantity;
        }

        public long getId() {
            return id;
        }

        public long getFoodId() {
            return foodId;
        }

        public String getFoodName() {
            return foodName;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
