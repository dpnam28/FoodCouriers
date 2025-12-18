package org.dpnam28.foodcouriers.ui.fooddetail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import org.dpnam28.foodcouriers.R;
import org.dpnam28.foodcouriers.ui.cart.CartActivity;
import org.dpnam28.foodcouriers.utils.ApiClient;
import org.dpnam28.foodcouriers.utils.ToastUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.Locale;

public class FoodDetailActivity extends AppCompatActivity implements FoodDetailContract.View {

    public static final String EXTRA_FOOD_ID = "extra_food_id";

    private ImageButton btnBack;
    private Button btnAddToCart;
    private ImageView imgFood;
    private TextView tvTitle;
    private TextView tvDescription;
    private TextView tvPrice;
    private ProgressBar progressBar;

    private FoodDetailContract.Presenter presenter;
    private ApiClient apiClient;
    private long foodId;
    private long userId;
    private String userRole;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_detail);
        assignView();
        apiClient = ApiClient.getInstance(this);
        initUserInfo();

        foodId = getIntent().getLongExtra(EXTRA_FOOD_ID, 0L);
        if (foodId == 0L) {
            ToastUtils.showTopToast(this, "Không tìm thấy thông tin món ăn", ToastUtils.TYPE_ERROR);
            finish();
            return;
        }

        presenter = new FoodDetailPresenter(this, this);

        btnBack.setOnClickListener(v -> finish());
        btnAddToCart.setOnClickListener(v -> addToCart());

        presenter.loadFood(foodId);
    }

    private void assignView() {
        btnBack = findViewById(R.id.btnBack);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        imgFood = findViewById(R.id.imgFood);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvPrice = findViewById(R.id.tvPrice);
        progressBar = findViewById(R.id.progressFoodDetail);
    }

    private void initUserInfo() {
        SharedPreferences prefs = getSharedPreferences("userInfo", MODE_PRIVATE);
        userRole = prefs.getString("role", "");
        String idValue = prefs.getString("id", "");
        try {
            userId = Long.parseLong(idValue);
        } catch (NumberFormatException e) {
            userId = 0L;
        }
        boolean isCustomer = "ROLE_CUSTOMER".equals(userRole);
        btnAddToCart.setVisibility(isCustomer ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showFood(FoodDetailContract.FoodDetail detail) {
        tvTitle.setText(detail.getName());
        tvDescription.setText(detail.getDescription());
        tvPrice.setText(currencyFormat.format(detail.getPrice()));
        Glide.with(this)
                .load(detail.getImageUrl())
                .centerCrop()
                .into(imgFood);
    }

    private void addToCart() {
        if (!"ROLE_CUSTOMER".equals(userRole)) {
            ToastUtils.showTopToast(this, "Chỉ khách hàng mới có thể thêm vào giỏ hàng", ToastUtils.TYPE_ERROR);
            return;
        }
        if (userId == 0L) {
            ToastUtils.showTopToast(this, "Không xác định được người dùng", ToastUtils.TYPE_ERROR);
            return;
        }
        JSONObject body = new JSONObject();
        try {
            body.put("userId", userId);
            body.put("foodId", foodId);
            body.put("quantity", 1);
        } catch (JSONException e) {
            ToastUtils.showTopToast(this, "Dữ liệu không hợp lệ", ToastUtils.TYPE_ERROR);
            return;
        }
        showLoading(true);
        apiClient.postJson("cart-items", body,
                response -> {
                    showLoading(false);
                    ToastUtils.showTopToast(this, getString(R.string.cart_add_success), ToastUtils.TYPE_SUCCESS);
                    startActivity(new Intent(this, CartActivity.class));
                },
                error -> {
                    showLoading(false);
                    ToastUtils.showTopToast(this, parseErrorMessage(error), ToastUtils.TYPE_ERROR);
                });
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
    public void showError(String message) {
        ToastUtils.showTopToast(this, message == null ? "Đã xảy ra lỗi" : message, ToastUtils.TYPE_ERROR);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detach();
        }
    }
}
