package org.dpnam28.foodcouriers.ui.fooddetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.dpnam28.foodcouriers.R;
import org.dpnam28.foodcouriers.ui.cart.CartActivity;
import org.dpnam28.foodcouriers.utils.ToastUtils;

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
    private long foodId;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_detail);
        assignView();

        foodId = getIntent().getLongExtra(EXTRA_FOOD_ID, 0L);
        if (foodId == 0L) {
            ToastUtils.showTopToast(this, "Không tìm thấy thông tin món ăn", ToastUtils.TYPE_ERROR);
            finish();
            return;
        }

        presenter = new FoodDetailPresenter(this, this);

        btnBack.setOnClickListener(v -> finish());
        btnAddToCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });

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
