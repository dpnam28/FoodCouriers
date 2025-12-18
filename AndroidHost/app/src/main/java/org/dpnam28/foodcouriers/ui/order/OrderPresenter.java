package org.dpnam28.foodcouriers.ui.order;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.VolleyError;

import org.dpnam28.foodcouriers.R;
import org.dpnam28.foodcouriers.utils.ApiClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class OrderPresenter implements OrderContract.Presenter {

    private static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    private static final String ROLE_RESTAURANT = "ROLE_RESTAURANT";
    private static final String ROLE_COURIER = "ROLE_COURIER";

    private final Context context;
    private final ApiClient apiClient;
    private OrderContract.View view;

    OrderPresenter(Context context, OrderContract.View view) {
        this.context = context.getApplicationContext();
        this.apiClient = ApiClient.getInstance(this.context);
        this.view = view;
    }

    @Override
    public void loadOrders(long userId, String role) {
        if (view == null) return;
        if (userId == 0L) {
            view.showError("Không xác định được người dùng");
            return;
        }
        String endpoint = buildOrdersEndpoint(userId, role);
        if (endpoint == null) {
            view.showError("Vai trò hiện chưa được hỗ trợ");
            return;
        }
        view.showLoading(true);
        apiClient.getJson(endpoint,
                response -> {
                    if (view == null) return;
                    view.showLoading(false);
                    JSONArray data = response.optJSONArray("data");
                    view.onOrdersLoaded(parseOrders(data));
                },
                error -> {
                    if (view == null) return;
                    view.showLoading(false);
                    view.showError(parseErrorMessage(error));
                });
    }

    @Override
    public void cancelOrder(long orderId, long actorId, String role) {
        if (view == null) return;
        String endpoint;
        if (ROLE_CUSTOMER.equals(role)) {
            endpoint = "orders/" + orderId + "/cancel?customerId=" + actorId;
        } else if (ROLE_RESTAURANT.equals(role)) {
            endpoint = "orders/" + orderId + "/cancel?restaurantId=" + actorId;
        } else {
            view.showError("Vai trò hiện chưa được hỗ trợ");
            return;
        }
        view.showLoading(true);
        apiClient.patchJson(endpoint, null,
                this::handleActionSuccess,
                this::handleActionError);
    }

    @Override
    public void acceptOrder(long orderId, long restaurantId) {
        if (view == null) return;
        String endpoint = "orders/" + orderId + "/accept?restaurantId=" + restaurantId;
        view.showLoading(true);
        apiClient.patchJson(endpoint, null,
                this::handleActionSuccess,
                this::handleActionError);
    }

    private void handleActionSuccess(JSONObject response) {
        if (view == null) return;
        view.showLoading(false);
        String message = response != null ? response.optString("message", "Thao tác thành công") : "Thao tác thành công";
        view.onOrderActionSuccess(message);
    }

    private void handleActionError(VolleyError error) {
        if (view == null) return;
        view.showLoading(false);
        view.showError(parseErrorMessage(error));
    }

    private String buildOrdersEndpoint(long userId, String role) {
        if (ROLE_CUSTOMER.equals(role)) {
            return "orders/customer/" + userId;
        } else if (ROLE_RESTAURANT.equals(role)) {
            return "orders/restaurant/" + userId;
        } else if (ROLE_COURIER.equals(role)) {
            return "orders/courier/" + userId;
        }
        return null;
    }

    private List<OrderModel> parseOrders(JSONArray array) {
        List<OrderModel> results = new ArrayList<>();
        if (array == null) {
            return results;
        }
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.optJSONObject(i);
            if (obj == null) continue;
            String status = obj.optString("status");
            results.add(new OrderModel(
                    obj.optLong("id"),
                    status,
                    translateStatus(status),
                    obj.optDouble("totalPrice"),
                    parseRestaurant(obj.optJSONObject("restaurant")),
                    parseCustomer(obj.optJSONObject("customer")),
                    parseCourier(obj.optJSONObject("courier")),
                    parseItems(obj.optJSONArray("orderDetails"))
            ));
        }
        return results;
    }

    private OrderModel.RestaurantInfo parseRestaurant(JSONObject obj) {
        if (obj == null) return null;
        return new OrderModel.RestaurantInfo(
                obj.optLong("id"),
                obj.optString("name"),
                obj.optString("address"),
                obj.optString("phoneNumber")
        );
    }

    private OrderModel.CustomerInfo parseCustomer(JSONObject obj) {
        if (obj == null) return null;
        return new OrderModel.CustomerInfo(
                obj.optLong("id"),
                obj.optString("fullName"),
                obj.optString("address"),
                obj.optString("phoneNumber")
        );
    }

    private OrderModel.CourierInfo parseCourier(JSONObject obj) {
        if (obj == null) return null;
        return new OrderModel.CourierInfo(
                obj.optLong("id"),
                obj.optString("fullName"),
                obj.optString("phoneNumber"),
                obj.optBoolean("isAvailable")
        );
    }

    private List<OrderModel.OrderItem> parseItems(JSONArray array) {
        List<OrderModel.OrderItem> items = new ArrayList<>();
        if (array == null) {
            return items;
        }
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.optJSONObject(i);
            if (obj == null) continue;
            JSONObject foodObj = obj.optJSONObject("food");
            long foodId = foodObj != null ? foodObj.optLong("id") : 0L;
            String foodName = foodObj != null ? foodObj.optString("name") : "";
            items.add(new OrderModel.OrderItem(
                    obj.optLong("id"),
                    foodId,
                    foodName,
                    obj.optInt("quantity")
            ));
        }
        return items;
    }

    private String translateStatus(String status) {
        if (context == null) {
            return status;
        }
        if ("ACCEPTED".equals(status)) {
            return context.getString(R.string.order_status_accepted);
        } else if ("DELIVERED".equals(status)) {
            return context.getString(R.string.order_status_delivered);
        } else if ("CANCELED".equals(status)) {
            return context.getString(R.string.order_status_canceled);
        }
        return context.getString(R.string.order_status_pending);
    }

    private String parseErrorMessage(VolleyError error) {
        if (error == null || error.networkResponse == null || error.networkResponse.data == null) {
            return "Đã xảy ra lỗi";
        }
        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
        try {
            JSONObject obj = new JSONObject(responseBody);
            String message = obj.optString("message");
            if (!TextUtils.isEmpty(message)) {
                return message;
            }
        } catch (JSONException ignored) {
            return responseBody;
        }
        return responseBody;
    }

    @Override
    public void detach() {
        view = null;
        apiClient.cancelAll();
    }
}
