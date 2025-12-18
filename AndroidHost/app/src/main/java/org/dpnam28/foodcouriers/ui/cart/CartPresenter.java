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
import java.util.List;

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
                    obj.optDouble("totalPrice")
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
