package org.dpnam28.foodcouriers.ui.cart;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.VolleyError;

import org.dpnam28.foodcouriers.utils.ApiClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class CartPresenter implements CartContract.Presenter {

    private final Context context;
    private final ApiClient apiClient;
    private CartContract.View view;

    CartPresenter(Context context, CartContract.View view) {
        this.context = context.getApplicationContext();
        this.apiClient = ApiClient.getInstance(this.context);
        this.view = view;
    }

    @Override
    public void loadCart(long userId) {
        if (view == null) return;
        view.showLoading(true);
        apiClient.getJson("cart-items/user/" + userId,
                response -> {
                    if (view == null) return;
                    view.showLoading(false);
                    JSONArray data = response.optJSONArray("data");
                    view.onCartItemsLoaded(parseCartItems(data));
                },
                error -> {
                    if (view == null) return;
                    view.showLoading(false);
                    view.showError(parseErrorMessage(error));
                });
    }

    @Override
    public void deleteCartItem(long cartItemId) {
        if (view == null) return;
        view.showLoading(true);
        apiClient.deleteJson("cart-items/" + cartItemId,
                response -> {
                    if (view == null) return;
                    view.showLoading(false);
                    view.onCartActionSuccess("Đã xóa món khỏi giỏ hàng");
                },
                error -> {
                    if (view == null) return;
                    view.showLoading(false);
                    view.showError(parseErrorMessage(error));
                });
    }

    @Override
    public void updateCartItem(long cartItemId, int quantity) {
        if (view == null) return;
        JSONObject body = new JSONObject();
        try {
            body.put("quantity", quantity);
        } catch (JSONException e) {
            view.showError("Dữ liệu không hợp lệ");
            return;
        }
        view.showLoading(true);
        apiClient.putJson("cart-items/" + cartItemId, body,
                response -> {
                    if (view == null) return;
                    view.showLoading(false);
                    view.onCartActionSuccess("Đã cập nhật số lượng");
                },
                error -> {
                    if (view == null) return;
                    view.showLoading(false);
                    view.showError(parseErrorMessage(error));
                });
    }

    @Override
    public void placeOrders(long customerId, List<CartItemModel> items) {
        if (view == null) return;
        if (items == null || items.isEmpty()) {
            view.showError("Giỏ hàng đang trống");
            return;
        }
        LinkedHashMap<Long, List<Long>> groupedIds = new LinkedHashMap<>();
        for (CartItemModel item : items) {
            long restaurantId = item.getRestaurantId();
            if (restaurantId == 0L) continue;
            List<Long> ids = groupedIds.computeIfAbsent(restaurantId, key -> new ArrayList<>());
            ids.add(item.getId());
        }
        if (groupedIds.isEmpty()) {
            view.showError("Không thể xác định nhà hàng cho đơn hàng");
            return;
        }
        view.showLoading(true);
        List<Map.Entry<Long, List<Long>>> entries = new ArrayList<>(groupedIds.entrySet());
        processOrderGroup(customerId, entries, 0);
    }

    private void processOrderGroup(long customerId, List<Map.Entry<Long, List<Long>>> entries, int index) {
        if (view == null) return;
        if (index >= entries.size()) {
            view.showLoading(false);
            view.onOrdersPlaced("Đặt hàng thành công");
            return;
        }
        Map.Entry<Long, List<Long>> entry = entries.get(index);
        long restaurantId = entry.getKey();
        JSONArray cartIds = new JSONArray(entry.getValue());
        JSONObject body = new JSONObject();
        try {
            body.put("customerId", customerId);
            body.put("restaurantId", restaurantId);
            body.put("cartItemIds", cartIds);
        } catch (JSONException e) {
            view.showLoading(false);
            view.showError("Không thể tạo dữ liệu đơn hàng");
            return;
        }
        apiClient.postJson("orders", body,
                response -> processOrderGroup(customerId, entries, index + 1),
                error -> {
                    if (view == null) return;
                    view.showLoading(false);
                    view.showError(parseErrorMessage(error));
                });
    }

    private List<CartItemModel> parseCartItems(JSONArray data) {
        List<CartItemModel> list = new ArrayList<>();
        if (data == null) {
            return list;
        }
        for (int i = 0; i < data.length(); i++) {
            JSONObject obj = data.optJSONObject(i);
            if (obj == null) continue;
            list.add(new CartItemModel(
                    obj.optLong("id"),
                    obj.optLong("foodId"),
                    obj.optString("foodName"),
                    obj.optInt("quantity"),
                    obj.optDouble("totalPrice"),
                    obj.optLong("restaurantId"),
                    obj.optString("restaurantName")
            ));
        }
        return list;
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
