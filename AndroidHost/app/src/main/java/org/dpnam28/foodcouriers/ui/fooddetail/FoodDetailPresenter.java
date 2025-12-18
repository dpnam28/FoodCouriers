package org.dpnam28.foodcouriers.ui.fooddetail;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.VolleyError;

import org.dpnam28.foodcouriers.utils.ApiClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class FoodDetailPresenter implements FoodDetailContract.Presenter {

    private final Context context;
    private final ApiClient apiClient;
    private FoodDetailContract.View view;

    public FoodDetailPresenter(Context context, FoodDetailContract.View view) {
        this.context = context.getApplicationContext();
        this.apiClient = ApiClient.getInstance(this.context);
        this.view = view;
    }

    @Override
    public void loadFood(long foodId) {
        if (view == null) return;
        view.showLoading(true);
        apiClient.getJson("foods/" + foodId,
                response -> {
                    if (view == null) return;
                    view.showLoading(false);
                    JSONObject data = response.optJSONObject("data");
                    if (data == null) {
                        view.showError("Không có dữ liệu món ăn");
                        return;
                    }
                    FoodDetailContract.FoodDetail detail = new FoodDetailContract.FoodDetail(
                            data.optString("name"),
                            data.optString("description"),
                            data.optDouble("price"),
                            data.optString("image")
                    );
                    view.showFood(detail);
                },
                error -> {
                    if (view == null) return;
                    view.showLoading(false);
                    view.showError(parseErrorMessage(error));
                });
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
