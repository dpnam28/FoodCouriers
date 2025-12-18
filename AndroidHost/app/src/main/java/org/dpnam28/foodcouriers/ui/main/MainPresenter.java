package org.dpnam28.foodcouriers.ui.main;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.VolleyError;

import org.dpnam28.foodcouriers.utils.ApiClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainPresenter implements MainContract.Presenter {

    private final Context context;
    private final ApiClient apiClient;
    private MainContract.View view;
    private long selectedCategoryId = -1L;

    public MainPresenter(Context context, MainContract.View view) {
        this.context = context.getApplicationContext();
        this.apiClient = ApiClient.getInstance(this.context);
        this.view = view;
    }

    @Override
    public void loadInitialData() {
        loadCategories();
    }

    private void loadCategories() {
        if (view == null) return;
        apiClient.getJson("categories",
                response -> {
                    if (view == null) return;
                    List<CategoryItem> categories = parseCategories(response.optJSONArray("data"));
                    if (categories.isEmpty()) {
                        selectedCategoryId = -1L;
                        view.showCategories(categories, selectedCategoryId);
                        view.showFoods(new ArrayList<>());
                        view.showEmptyFoods(true);
                    } else {
                        selectedCategoryId = categories.get(0).getId();
                        view.showCategories(categories, selectedCategoryId);
                        loadFoodsByCategory(selectedCategoryId);
                    }
                },
                error -> {
                    if (view == null) return;
                    view.showError(parseErrorMessage(error));
                });
    }

    @Override
    public void selectCategory(long categoryId) {
        if (categoryId <= 0 || categoryId == selectedCategoryId) {
            return;
        }
        selectedCategoryId = categoryId;
        loadFoodsByCategory(categoryId);
    }

    private void loadFoodsByCategory(long categoryId) {
        if (view == null) return;
        view.showFoodsLoading(true);
        apiClient.getJson("categories/" + categoryId + "/foods",
                response -> {
                    if (view == null) return;
                    view.showFoodsLoading(false);
                    List<FoodItem> foods = parseFoods(response.optJSONArray("data"));
                    view.showFoods(foods);
                    view.showEmptyFoods(foods.isEmpty());
                },
                error -> {
                    if (view == null) return;
                    view.showFoodsLoading(false);
                    view.showEmptyFoods(true);
                    view.showError(parseErrorMessage(error));
                });
    }

    private List<CategoryItem> parseCategories(JSONArray data) {
        List<CategoryItem> categories = new ArrayList<>();
        if (data == null) {
            return categories;
        }
        for (int i = 0; i < data.length(); i++) {
            JSONObject obj = data.optJSONObject(i);
            if (obj == null) continue;
            categories.add(new CategoryItem(
                    obj.optLong("id"),
                    obj.optString("name")
            ));
        }
        return categories;
    }

    private List<FoodItem> parseFoods(JSONArray data) {
        List<FoodItem> foods = new ArrayList<>();
        if (data == null) {
            return foods;
        }
        for (int i = 0; i < data.length(); i++) {
            JSONObject obj = data.optJSONObject(i);
            if (obj == null || !obj.optBoolean("isActive", true)) continue;
            foods.add(new FoodItem(
                    obj.optLong("id"),
                    obj.optString("name"),
                    obj.optString("description"),
                    obj.optDouble("price"),
                    obj.optString("image")
            ));
        }
        return foods;
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
