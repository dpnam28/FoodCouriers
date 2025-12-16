package org.dpnam28.foodcouriers.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.VolleyError;

import org.dpnam28.foodcouriers.utils.ApiClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class LoginPresenter implements LoginContract.Presenter {

    private static final String TAG = "LoginPresenter";

    private final Context context;
    private final ApiClient apiClient;
    private LoginContract.View view;

    public LoginPresenter(Context context, LoginContract.View view) {
        this.context = context.getApplicationContext();
        this.view = view;
        this.apiClient = ApiClient.getInstance(this.context);
    }

    @Override
    public void register(LoginContract.UserForm form) {
        if (view == null) {
            return;
        }

        JSONObject body = new JSONObject();
        try {
            body.put("fullName", form.getFullName());
            body.put("email", form.getEmail());
            body.put("password", form.getPassword());
            body.put("phoneNumber", form.getPhoneNumber());
            body.put("address", form.getAddress());
            body.put("role", form.getRole());
            body.put("locationId", form.getLocationId());
        } catch (JSONException e) {
            Log.e(TAG, "register: cannot build request body", e);
            view.onRegisterError("Không thể tạo dữ liệu đăng ký");
            return;
        }

        view.showLoading(true);
        apiClient.postJson("users", body,
                response -> {
                    if (view == null) return;
                    view.showLoading(false);
                    view.onRegisterSuccess();
                },
                error -> {
                    if (view == null) return;
                    view.showLoading(false);
                    String message = parseErrorMessage(error);
                    Log.e(TAG, "register: " + message, error);
                    view.onRegisterError(message);
                });
    }

    @Override
    public void login(LoginContract.UserLoginForm form) {
        if (view == null) return;

        JSONObject body = new JSONObject();
        try {
            body.put("email", form.getEmail());
            body.put("password", form.getPassword());
        } catch (JSONException e) {
            Log.e(TAG, "register: cannot build request body", e);
            view.onRegisterError("Không thể tạo dữ liệu đăng ký");
            return;
        }
        view.showLoading(true);
        apiClient.postJson("auth/login", body,
                response -> {
                    if (view == null) return;
                    view.showLoading(false);
                    try {
                        saveLoginData(response.getJSONObject("data"));
                        view.onRegisterSuccess();
                    } catch (JSONException e) {
                        Log.e(TAG, "register: cannot parse response", e);
                        view.onLoginError("Lỗi xử lý dữ liệu đăng nhập");
                    }
                    view.onLoginSuccess();
                }, error -> {
                    if (view == null) return;
                    view.showLoading(false);
                    String message = parseErrorMessage(error);
                    Log.e(TAG, "Login: " + message, error);
                    view.onLoginError(message);
                });
    }

    private String parseErrorMessage(VolleyError error) {
        if (error == null) {
            return "Đã có lỗi xảy ra";
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

        if (error.getMessage() != null) {
            return error.getMessage();
        }

        return "Không thể kết nối máy chủ";
    }

    private void saveLoginData(JSONObject json) {
        SharedPreferences userInfoPrefs = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfoPrefs.edit();

        String id = json.optString("id", "");
        String email = json.optString("email", "");
        String fullName = json.optString("fullName", "");
        String phoneNumber = json.optString("phoneNumber", "");
        String role = json.optString("role", "");
        String address = json.optString("address", "");
        JSONObject locationObj = json.optJSONObject("location");
        String location = "";
        long locationId = 0L;
        if (locationObj != null) {
            location = locationObj.optString("city", "");
            if (locationObj.has("id")) {
                locationId = locationObj.optLong("id");
            }
        }

        editor.putString("id", id);
        editor.putString("email", email);
        editor.putString("fullName", fullName);
        editor.putString("phoneNumber", phoneNumber);
        editor.putString("role", role);
        editor.putString("address", address);
        editor.putString("location", location);
        editor.putLong("locationId", locationId);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    @Override
    public void detach() {
        view = null;
        apiClient.cancelAll();
    }
}
