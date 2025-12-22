package org.dpnam28.foodcouriers.ui.menu;

import java.util.List;

public class RestaurantMenuContract {
    public interface View {
        void showLoading(boolean isLoading);
        void onFoodsLoaded(List<RestaurantMenuItem> foods);
        void onError(String message);
    }
    public interface Presenter {
        void getFoods(long restaurantId);
        void detach();
    }
}
