package org.dpnam28.foodcouriers.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.dpnam28.foodcouriers.BuildConfig;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiClient {

    private static final String BASE_URL = BuildConfig.API_BASE_URL;
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String MIME_JSON = "application/json";

    private static ApiClient instance;
    private final RequestQueue requestQueue;

    private ApiClient(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
        }
        return instance;
    }

    public String getBaseUrl() {
        return BASE_URL;
    }

    public void addToRequestQueue(Request<?> request) {
        request.setTag(ApiClient.class.getSimpleName());
        requestQueue.add(request);
    }

    public void cancelAll() {
        requestQueue.cancelAll(ApiClient.class.getSimpleName());
    }

    public void postJson(
            String endpoint,
            JSONObject body,
            Response.Listener<JSONObject> listener,
            Response.ErrorListener errorListener
    ) {
        addToRequestQueue(buildJsonRequest(Request.Method.POST, endpoint, body, listener, errorListener));
    }

    public void getJson(
            String endpoint,
            Response.Listener<JSONObject> listener,
            Response.ErrorListener errorListener
    ) {
        addToRequestQueue(buildJsonRequest(Request.Method.GET, endpoint, null, listener, errorListener));
    }

    public void putJson(
            String endpoint,
            JSONObject body,
            Response.Listener<JSONObject> listener,
            Response.ErrorListener errorListener
    ) {
        addToRequestQueue(buildJsonRequest(Request.Method.PUT, endpoint, body, listener, errorListener));
    }

    public void deleteJson(
            String endpoint,
            Response.Listener<JSONObject> listener,
            Response.ErrorListener errorListener
    ) {
        addToRequestQueue(buildJsonRequest(Request.Method.DELETE, endpoint, null, listener, errorListener));
    }

    public void putMultipart(
            String endpoint,
            Map<String, String> params,
            Map<String, MultipartRequest.DataPart> fileParts,
            Response.Listener<JSONObject> listener,
            Response.ErrorListener errorListener
    ) {
        addToRequestQueue(buildMultipartRequest(Request.Method.PUT, endpoint, params, fileParts, listener, errorListener));
    }

    public void postMultipart(
            String endpoint,
            Map<String, String> params,
            Map<String, MultipartRequest.DataPart> fileParts,
            Response.Listener<JSONObject> listener,
            Response.ErrorListener errorListener
    ) {
        addToRequestQueue(buildMultipartRequest(Request.Method.POST, endpoint, params, fileParts, listener, errorListener));
    }

    private MultipartRequest buildMultipartRequest(
            int method,
            String endpoint,
            Map<String, String> params,
            Map<String, MultipartRequest.DataPart> fileParts,
            Response.Listener<JSONObject> listener,
            Response.ErrorListener errorListener
    ) {
        return new MultipartRequest(
                method,
                BASE_URL + endpoint,
                params,
                fileParts,
                listener,
                errorListener
        );
    }

    private JsonObjectRequest buildJsonRequest(
            int method,
            String endpoint,
            JSONObject body,
            Response.Listener<JSONObject> listener,
            Response.ErrorListener errorListener
    ) {
        return new JsonObjectRequest(
                method,
                BASE_URL + endpoint,
                body,
                listener,
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put(HEADER_CONTENT_TYPE, MIME_JSON);
                return headers;
            }
        };
    }
}
