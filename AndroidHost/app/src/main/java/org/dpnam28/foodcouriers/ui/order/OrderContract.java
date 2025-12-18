package org.dpnam28.foodcouriers.ui.order;

import java.util.List;

class OrderContract {

    interface View {
        void showLoading(boolean loading);

        void onOrdersLoaded(List<OrderModel> orders);

        void onOrderActionSuccess(String message);

        void showError(String message);
    }

    interface Presenter {
        void loadOrders(long userId, String role);

        void cancelOrder(long orderId, long actorId, String role);

        void acceptOrder(long orderId, long restaurantId);

        void deliverOrder(long orderId, long courierId);

        void detach();
    }
}
