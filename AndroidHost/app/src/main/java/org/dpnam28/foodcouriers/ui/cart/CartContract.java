package org.dpnam28.foodcouriers.ui.cart;

import java.util.List;

class CartContract {

    public interface View {
        void showLoading(boolean loading);

        void onCartItemsLoaded(List<CartItemModel> items);

        void onCartActionSuccess(String message);

        void showError(String message);
    }

    public interface Presenter {
        void loadCart(long userId);

        void deleteCartItem(long cartItemId);

        void updateCartItem(long cartItemId, int quantity);

        void detach();
    }
}
