package org.dpnam28.foodcouriers.ui.profile;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.dpnam28.foodcouriers.utils.ApiClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class ProfilePresenter implements ProfileContract.Presenter {

    private static final String TAG = "ProfilePresenter";

    private final ApiClient apiClient;
    private ProfileContract.View view;

    public ProfilePresenter(Context context, ProfileContract.View view) {
        this.apiClient = ApiClient.getInstance(context.getApplicationContext());
        this.view = view;
    }

    @Override
    public void getUser(long userId) {
        if (view == null) {
            return;
        }
        view.showLoading(true);
        apiClient.getJson("users/" + userId,
                response -> {
                    if (view == null) return;
                    view.showLoading(false);
                    JSONObject data = response.optJSONObject("data");
                    ProfileContract.UserDetail detail = mapUserDetail(data);
                    if (detail == null) {
                        view.onError("Không thể tải thông tin người dùng");
                        return;
                    }
                    view.onUserLoaded(detail);
                },
                error -> handleError(error, "Không thể tải thông tin người dùng"));
    }

    @Override
    public void updateUser(ProfileContract.UpdateForm form) {
        if (view == null) {
            return;
        }
        JSONObject body = new JSONObject();
        try {
            body.put("password", form.getPassword());
            body.put("fullName", form.getFullName());
            body.put("phoneNumber", form.getPhoneNumber());
            body.put("address", form.getAddress());
            body.put("role", form.getRole());
            if ("ROLE_RESTAURANT".equals(form.getRole())) {
                body.put("description", form.getDescription() == null ? "" : form.getDescription());
                body.put("bannerImage", form.getBannerImage() == null ? "" : form.getBannerImage());
                body.put("deliveryFee", form.getDeliveryFee());
            }
        } catch (JSONException e) {
            Log.e(TAG, "updateUser: cannot build request body", e);
            view.onError("Không thể tạo dữ liệu cập nhật");
            return;
        }

        view.showLoading(true);
        apiClient.putJson("users/" + form.getUserId(),
                body,
                response -> {
                    if (view == null) return;
                    view.showLoading(false);
                    JSONObject data = response.optJSONObject("data");
                    ProfileContract.UserDetail detail = mapUserDetail(data);
                    if (detail == null) {
                        view.onError("Cập nhật thông tin thất bại");
                        return;
                    }
                    view.onUpdateSuccess(detail);
                },
                error -> handleError(error, "Cập nhật thông tin thất bại"));
    }

    private void handleError(VolleyError error, String fallbackMessage) {
        if (view == null) {
            return;
        }
        view.showLoading(false);
        String message = parseErrorMessage(error);
        if (message == null || message.isEmpty()) {
            message = fallbackMessage;
        }
        Log.e(TAG, message, error);
        view.onError(message);
    }

    private String parseErrorMessage(VolleyError error) {
        if (error == null) {
            return null;
        }
        if (error.networkResponse != null && error.networkResponse.data != null) {
            String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
            try {
                JSONObject errorJson = new JSONObject(responseBody);
                String message = errorJson.optString("message");
                if (!message.isEmpty()) {
                    return message;
                }
            } catch (JSONException ignored) {
                return responseBody;
            }
            return responseBody;
        }
        return error.getMessage();
    }

    private ProfileContract.UserDetail mapUserDetail(JSONObject data) {
        if (data == null) {
            return null;
        }
        JSONObject location = data.optJSONObject("location");
        long locationId = 0L;
        String locationName = "";
        if (location != null) {
            if (location.has("id")) {
                locationId = location.optLong("id");
            }
            locationName = location.optString("city", location.optString("name", ""));
        }
        String description = data.isNull("description") ? null : data.optString("description");
        String bannerImage = data.isNull("bannerImage") ? null : data.optString("bannerImage");
        Double deliveryFee = null;
        if (data.has("deliveryFee") && !data.isNull("deliveryFee")) {
            deliveryFee = data.optDouble("deliveryFee");
        }

        return new ProfileContract.UserDetail(
                data.optLong("id"),
                data.optString("email"),
                data.optString("fullName"),
                data.optString("phoneNumber"),
                data.optString("address"),
                data.optString("role"),
                locationId,
                locationName,
                description,
                bannerImage,
                deliveryFee
        );
    }

    @Override
    public void detach() {
        view = null;
        apiClient.cancelAll();
    }
}
