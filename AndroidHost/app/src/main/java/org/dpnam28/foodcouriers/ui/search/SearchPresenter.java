package org.dpnam28.foodcouriers.ui.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.VolleyError;

import org.dpnam28.foodcouriers.utils.ApiClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SearchPresenter implements SearchContract.Presenter {

    private final Context context;
    private final ApiClient apiClient;
    private SearchContract.View view;
    private final SharedPreferences sharedPreferences;

    public SearchPresenter(Context context, SearchContract.View view) {
        this.context = context.getApplicationContext();
        this.view = view;
        this.apiClient = ApiClient.getInstance(this.context);
        this.sharedPreferences = this.context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    @Override
    public void search(String keyword, SearchContract.SearchType type) {
        if (view == null) {
            return;
        }
        view.showLoading(true);
        switch (type) {
            case RESTAURANT:
                fetchRestaurants(keyword);
                break;
            case FOOD:
            default:
                fetchFoods(keyword);
                break;
        }
    }

    private void fetchFoods(String keyword) {
        String encodedKeyword = keyword == null ? "" : Uri.encode(keyword);
        String endpoint = "foods/search?keyword=" + encodedKeyword;
        apiClient.getJson(endpoint,
                response -> {
                    if (view == null) return;
                    view.showLoading(false);
                    List<SearchResultItem> results = parseFoodResults(response.optJSONArray("data"));
                    if (results.isEmpty()) {
                        view.showEmptyState();
                    } else {
                        view.showResults(results, SearchContract.SearchType.FOOD);
                    }
                },
                error -> {
                    if (view == null) return;
                    view.showLoading(false);
                    view.showError(parseErrorMessage(error));
                });
    }

    private void fetchRestaurants(String keyword) {
        String encodedKeyword = keyword == null ? "" : Uri.encode(keyword);
        String location = sharedPreferences.getString("location", "");
        String encodedLocation = Uri.encode(location);
        String endpoint = "restaurants/search?name=" + encodedKeyword + "&location=" + encodedLocation;
        apiClient.getJson(endpoint,
                response -> {
                    if (view == null) return;
                    view.showLoading(false);
                    List<SearchResultItem> results = parseRestaurantResults(response.optJSONArray("data"));
                    if (results.isEmpty()) {
                        view.showEmptyState();
                    } else {
                        view.showResults(results, SearchContract.SearchType.RESTAURANT);
                    }
                },
                error -> {
                    if (view == null) return;
                    view.showLoading(false);
                    view.showError(parseErrorMessage(error));
                });
    }

    private List<SearchResultItem> parseFoodResults(JSONArray data) {
        List<SearchResultItem> list = new ArrayList<>();
        if (data == null) return list;
        for (int i = 0; i < data.length(); i++) {
            JSONObject obj = data.optJSONObject(i);
            if (obj == null) continue;
            list.add(new SearchResultItem(
                    obj.optLong("id"),
                    obj.optString("name"),
                    obj.optString("description"),
                    obj.has("price") ? obj.optDouble("price") : null,
                    obj.optString("image"),
                    SearchContract.SearchType.FOOD
            ));
        }
        return list;
    }

    private List<SearchResultItem> parseRestaurantResults(JSONArray data) {
        List<SearchResultItem> list = new ArrayList<>();
        if (data == null) return list;
        for (int i = 0; i < data.length(); i++) {
            JSONObject obj = data.optJSONObject(i);
            if (obj == null) continue;
            String description = obj.optString("description");
            if (TextUtils.isEmpty(description)) {
                description = obj.optString("address");
            }
            list.add(new SearchResultItem(
                    obj.optLong("id"),
                    obj.optString("name"),
                    description,
                    null,
                    obj.optString("bannerImage"),
                    SearchContract.SearchType.RESTAURANT
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
