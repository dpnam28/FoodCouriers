package org.dpnam28.foodcouriers.ui.menu;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;

import org.dpnam28.foodcouriers.utils.ApiClient;
import org.dpnam28.foodcouriers.utils.MultipartRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodEditorPresenter implements FoodEditorContract.Presenter {

    private static final String TAG = "FoodEditorPresenter";

    private final Context context;
    private final ApiClient apiClient;
    private FoodEditorContract.View view;
    private boolean isSaving;

    public FoodEditorPresenter(Context context, FoodEditorContract.View view) {
        this.context = context.getApplicationContext();
        this.apiClient = ApiClient.getInstance(this.context);
        this.view = view;
    }

    @Override
    public void loadCategories() {
        if (view == null) return;
        apiClient.getJson("categories",
                response -> {
                    List<FoodEditorContract.CategoryOption> options = parseCategories(response.optJSONArray("data"));
                    view.onCategoriesLoaded(options);
                },
                error -> view.onError(parseErrorMessage(error)));
    }

    @Override
    public void loadFoodDetail(long foodId) {
        if (view == null) return;
        view.showLoading(true);
        apiClient.getJson("foods/" + foodId,
                response -> {
                    view.showLoading(false);
                    JSONObject data = response.optJSONObject("data");
                    FoodEditorContract.FoodDetail detail = parseFoodDetail(data);
                    if (detail == null) {
                        view.onError("Không thể tải món ăn");
                        return;
                    }
                    view.onFoodLoaded(detail);
                },
                error -> {
                    view.showLoading(false);
                    view.onError(parseErrorMessage(error));
                });
    }

    @Override
    public void createFood(FoodEditorContract.Form form, Uri imageUri) {
        if (view == null || isSaving) return;
        isSaving = true;
        Map<String, String> params = buildParamsForForm(form, true);
        Map<String, MultipartRequest.DataPart> files = buildImagePart(imageUri);
        view.showLoading(true);
        apiClient.postMultipart(
                "foods",
                params,
                files,
                response -> {
                    isSaving = false;
                    view.showLoading(false);
                    view.onSaveSuccess();
                },
                error -> {
                    isSaving = false;
                    view.showLoading(false);
                    view.onError(parseErrorMessage(error));
                }
        );
    }

    @Override
    public void updateFood(long foodId, FoodEditorContract.Form form, Uri imageUri) {
        if (view == null || isSaving) return;
        isSaving = true;
        Map<String, String> params = buildParamsForForm(form, false);
        Map<String, MultipartRequest.DataPart> files = buildImagePart(imageUri);
        view.showLoading(true);
        apiClient.putMultipart(
                "foods/" + foodId,
                params,
                files,
                response -> {
                    isSaving = false;
                    view.showLoading(false);
                    view.onSaveSuccess();
                },
                error -> {
                    isSaving = false;
                    view.showLoading(false);
                    view.onError(parseErrorMessage(error));
                }
        );
    }

    private Map<String, String> buildParamsForForm(FoodEditorContract.Form form, boolean includeRestaurant) {
        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(form.getName())) {
            params.put("name", form.getName());
        }
        if (!TextUtils.isEmpty(form.getDescription())) {
            params.put("description", form.getDescription());
        }
        params.put("price", String.valueOf(form.getPrice()));
        params.put("isActive", String.valueOf(form.isActive()));
        params.put("categoryId", String.valueOf(form.getCategoryId()));
        if (includeRestaurant) {
            long restaurantId = getRestaurantId();
            params.put("restaurantId", String.valueOf(restaurantId));
        }
        return params;
    }

    private Map<String, MultipartRequest.DataPart> buildImagePart(Uri uri) {
        if (uri == null) {
            return null;
        }
        try {
            byte[] bytes = readBytes(uri);
            if (bytes == null || bytes.length == 0) {
                return null;
            }
            String fileName = resolveFileName(uri);
            if (TextUtils.isEmpty(fileName)) {
                fileName = "food.jpg";
            }
            String mimeType = resolveMimeType(uri);
            MultipartRequest.DataPart part = new MultipartRequest.DataPart(fileName, bytes, mimeType);
            Map<String, MultipartRequest.DataPart> files = new HashMap<>();
            files.put("image", part);
            return files;
        } catch (IOException e) {
            Log.e(TAG, "buildImagePart: cannot read uri", e);
            return null;
        }
    }

    private long getRestaurantId() {
        SharedPreferences prefs = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String id = prefs.getString("id", "");
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            return 0L;
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

    private List<FoodEditorContract.CategoryOption> parseCategories(JSONArray data) {
        List<FoodEditorContract.CategoryOption> list = new ArrayList<>();
        if (data == null) {
            return list;
        }
        for (int i = 0; i < data.length(); i++) {
            JSONObject obj = data.optJSONObject(i);
            if (obj == null) continue;
            list.add(new FoodEditorContract.CategoryOption(
                    obj.optLong("id"),
                    obj.optString("name")
            ));
        }
        return list;
    }

    private FoodEditorContract.FoodDetail parseFoodDetail(JSONObject data) {
        if (data == null) {
            return null;
        }
        return new FoodEditorContract.FoodDetail(
                data.optLong("id"),
                data.optString("name"),
                data.optString("description"),
                data.optLong("categoryId"),
                data.optDouble("price"),
                data.optBoolean("isActive", true),
                data.optString("image")
        );
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

    @Override
    public void detach() {
        view = null;
        isSaving = false;
        apiClient.cancelAll();
    }
}
