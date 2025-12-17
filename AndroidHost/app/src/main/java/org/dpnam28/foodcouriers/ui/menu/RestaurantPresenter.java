package org.dpnam28.foodcouriers.ui.menu;

import android.content.Context;

import org.dpnam28.foodcouriers.utils.ApiClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class RestaurantPresenter implements RestaurantMenuContract.Presenter{

    private final Context context;
    private RestaurantMenuContract.View view;
    private final ApiClient apiClient;

    public RestaurantPresenter(Context context, RestaurantMenuContract.View view) {
        this.context = context.getApplicationContext();
        this.view = view;
        this.apiClient = ApiClient.getInstance(this.context);
    }


    @Override
    public void getFoods(long restaurantId, List<RestaurantMenuItem> foods) {
        view.showLoading(true);
        apiClient.getJson("restaurants/" + restaurantId + "/foods",
                response -> {
                    view.showLoading(false);
                    parseFoods(response.optJSONArray("data"), foods);
                },
                error -> {
                    view.showLoading(false);
                    view.onError("Không thể tải danh sách món ăn");
                });
    }

    private void parseFoods(JSONArray data, List<RestaurantMenuItem> allFoods) {
        allFoods.clear();
        if (data != null) {
            for (int i = 0; i < data.length(); i++) {
                JSONObject obj = data.optJSONObject(i);
                if (obj == null) continue;
                if (!obj.optBoolean("isActive", true)) continue;
                RestaurantMenuItem item = new RestaurantMenuItem(
                        obj.optLong("id"),
                        obj.optString("name"),
                        obj.optString("description"),
                        obj.optDouble("price"),
                        obj.optString("image")
                );
                allFoods.add(item);
            }
        }
        view.onFoodsLoaded(allFoods);
    }

    @Override
    public void detach() {
        view = null;
        apiClient.cancelAll();
    }
}
