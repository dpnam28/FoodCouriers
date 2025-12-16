package org.dpnam28.foodcouriers.ui.profile;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;

import org.dpnam28.foodcouriers.utils.ApiClient;
import org.dpnam28.foodcouriers.utils.MultipartRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ProfilePresenter implements ProfileContract.Presenter {

    private static final String TAG = "ProfilePresenter";

    private final Context context;
    private final ApiClient apiClient;
    private ProfileContract.View view;

    public ProfilePresenter(Context context, ProfileContract.View view) {
        this.context = context.getApplicationContext();
        this.apiClient = ApiClient.getInstance(this.context);
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
    public void updateUser(ProfileContract.UpdateForm form, Uri bannerImageUri) {
        if (view == null) {
            return;
        }

        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(form.getPassword())) {
            params.put("password", form.getPassword());
        }
        params.put("fullName", form.getFullName());
        params.put("phoneNumber", form.getPhoneNumber());
        params.put("address", form.getAddress());

        if (form.isRestaurant()) {
            params.put("description", form.getDescription() == null ? "" : form.getDescription());
            if (form.getDeliveryFee() != null) {
                params.put("deliveryFee", String.valueOf(form.getDeliveryFee()));
            }
        }

        Map<String, MultipartRequest.DataPart> files = new HashMap<>();
        if (bannerImageUri != null) {
            MultipartRequest.DataPart dataPart = buildDataPart("banner.jpg", bannerImageUri);
            if (dataPart == null) {
                view.onError("Không thể đọc ảnh đã chọn");
                return;
            }
            files.put("bannerImage", dataPart);
        }

        view.showLoading(true);
        apiClient.putMultipart(
                "users/" + form.getUserId(),
                params,
                files.isEmpty() ? null : files,
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
                error -> handleError(error, "Cập nhật thông tin thất bại")
        );
    }

    private MultipartRequest.DataPart buildDataPart(String fallbackName, Uri uri) {
        try {
            byte[] bytes = readBytes(uri);
            if (bytes == null || bytes.length == 0) {
                return null;
            }
            String fileName = resolveFileName(uri);
            if (TextUtils.isEmpty(fileName)) {
                fileName = fallbackName;
            }
            String mimeType = resolveMimeType(uri);
            return new MultipartRequest.DataPart(fileName, bytes, mimeType);
        } catch (IOException e) {
            Log.e(TAG, "buildDataPart: cannot read file", e);
            return null;
        }
    }

    private byte[] readBytes(Uri uri) throws IOException {
        ContentResolver resolver = context.getContentResolver();
        try (InputStream inputStream = resolver.openInputStream(uri);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            if (inputStream == null) {
                return null;
            }
            byte[] buffer = new byte[4096];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
            }
            return bos.toByteArray();
        }
    }

    private String resolveFileName(Uri uri) {
        final String[] projection = {OpenableColumns.DISPLAY_NAME};
        ContentResolver resolver = context.getContentResolver();
        try (Cursor cursor = resolver.query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (idx >= 0) {
                    return cursor.getString(idx);
                }
            }
        }
        return null;
    }

    private String resolveMimeType(Uri uri) {
        String type = context.getContentResolver().getType(uri);
        return type != null ? type : "application/octet-stream";
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
