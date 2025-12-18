package org.dpnam28.foodcouriers.ui.restaurant;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.VolleyError;

import org.dpnam28.foodcouriers.ui.menu.RestaurantMenuItem;
import org.dpnam28.foodcouriers.utils.ApiClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RestaurantDetailPresenter implements RestaurantDetailContract.Presenter {

    private final Context context;
    private final ApiClient apiClient;
    private RestaurantDetailContract.View view;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public RestaurantDetailPresenter(Context context, RestaurantDetailContract.View view) {
        this.context = context.getApplicationContext();
        this.view = view;
        this.apiClient = ApiClient.getInstance(this.context);
    }

    @Override
    public void loadRestaurant(long id) {
        if (view == null) return;
        view.showLoading(true);
        apiClient.getJson("restaurants/" + id,
                response -> {
                    if (view == null) return;
                    view.showLoading(false);
                    JSONObject data = response.optJSONObject("data");
                    if (data == null) {
                        view.showError("Không tìm thấy nhà hàng");
                        return;
                    }
                    JSONObject restaurantObj = data.optJSONObject("restaurant");
                    JSONArray foodArray = data.optJSONArray("foods");
                    RestaurantDetailContract.RestaurantInfo info = parseRestaurantInfo(restaurantObj);
                    List<RestaurantMenuItem> foods = parseFoods(foodArray);
                    view.showRestaurantInfo(info);
                    view.showFoods(foods);
                    view.showNoFoodsMessage(foods.isEmpty());
                },
                error -> {
                    if (view == null) return;
                    view.showLoading(false);
                    view.showError(parseErrorMessage(error));
                });
    }

    private RestaurantDetailContract.RestaurantInfo parseRestaurantInfo(JSONObject obj) {
        if (obj == null) {
            return new RestaurantDetailContract.RestaurantInfo("", "", "", "", "", "", null);
        }
        String deliveryFeeValue = currencyFormat.format(obj.optDouble("deliveryFee"));
        return new RestaurantDetailContract.RestaurantInfo(
                obj.optString("name"),
                obj.optString("description"),
                obj.optString("address"),
                obj.optString("phoneNumber"),
                obj.optString("location"),
                deliveryFeeValue,
                obj.optString("bannerImage")
        );
    }

    private List<RestaurantMenuItem> parseFoods(JSONArray foodsJson) {
        List<RestaurantMenuItem> list = new ArrayList<>();
        if (foodsJson == null) {
            return list;
        }
        for (int i = 0; i < foodsJson.length(); i++) {
            JSONObject obj = foodsJson.optJSONObject(i);
            if (obj == null || !obj.optBoolean("isActive", true)) continue;
            list.add(new RestaurantMenuItem(
                    obj.optLong("id"),
                    obj.optString("name"),
                    obj.optString("description"),
                    obj.optDouble("price"),
                    obj.optString("image")
            ));
        }
        return list;
    }

    private String parseErrorMessage(VolleyError error) {
        if (error == null) {
            return "Đã xảy ra lỗi";
        }
        if (error.networkResponse != null && error.networkResponse.data != null) {
            String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
            try {
                JSONObject errorJson = new JSONObject(responseBody);
                String message = errorJson.optString("message");
                if (!TextUtils.isEmpty(message)) {
                    return message;
                }
            } catch (JSONException ignored) {
                return responseBody;
            }
            return responseBody;
        }
        return error.getMessage();
    }

    @Override
    public void detach() {
        view = null;
        apiClient.cancelAll();
    }
}
