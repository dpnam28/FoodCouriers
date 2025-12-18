package org.dpnam28.foodcouriers.ui.order;

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

class SelectCourierPresenter implements SelectCourierContract.Presenter {

    private final ApiClient apiClient;
    private SelectCourierContract.View view;

    SelectCourierPresenter(Context context, SelectCourierContract.View view) {
        this.apiClient = ApiClient.getInstance(context.getApplicationContext());
        this.view = view;
    }

    @Override
    public void loadCouriers(String location) {
        if (view == null) return;
        JSONObject body = new JSONObject();
        try {
            body.put("location", location == null ? "" : location);
        } catch (JSONException ignored) {
        }
        view.showLoading(true);
        apiClient.postJson("orders/couriers/by-location", body,
                response -> {
                    if (view == null) return;
                    view.showLoading(false);
                    JSONArray data = response.optJSONArray("data");
                    view.showCouriers(parseCouriers(data));
                },
                error -> {
                    if (view == null) return;
                    view.showLoading(false);
                    view.showError(parseErrorMessage(error));
                });
    }

    @Override
    public void assignCourier(long orderId, long restaurantId, long courierId) {
        if (view == null) return;
        view.showLoading(true);
        String endpoint = "orders/" + orderId + "/assign-courier?restaurantId=" + restaurantId + "&courierId=" + courierId;
        apiClient.patchJson(endpoint, null,
                response -> {
                    if (view == null) return;
                    view.showLoading(false);
                    String message = response != null ? response.optString("message") : null;
                    view.onAssignmentSuccess(TextUtils.isEmpty(message) ? "Đã gán shipper" : message);
                },
                error -> {
                    if (view == null) return;
                    view.showLoading(false);
                    view.showError(parseErrorMessage(error));
                });
    }

    private List<SelectCourierContract.CourierItem> parseCouriers(JSONArray data) {
        List<SelectCourierContract.CourierItem> list = new ArrayList<>();
        if (data == null) {
            return list;
        }
        for (int i = 0; i < data.length(); i++) {
            JSONObject obj = data.optJSONObject(i);
            if (obj == null || !obj.optBoolean("isAvailable", true)) {
                continue;
            }
            list.add(new SelectCourierContract.CourierItem(
                    obj.optLong("id"),
                    obj.optString("fullName"),
                    obj.optString("phoneNumber"),
                    obj.optString("location")
            ));
        }
        return list;
    }

    private String parseErrorMessage(VolleyError error) {
        if (error == null || error.networkResponse == null || error.networkResponse.data == null) {
            return "Đã xảy ra lỗi";
        }
        String body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
        try {
            JSONObject obj = new JSONObject(body);
            String message = obj.optString("message");
            if (!TextUtils.isEmpty(message)) {
                return message;
            }
        } catch (JSONException ignored) {
            return body;
        }
        return body;
    }

    @Override
    public void detach() {
        view = null;
        apiClient.cancelAll();
    }
}
