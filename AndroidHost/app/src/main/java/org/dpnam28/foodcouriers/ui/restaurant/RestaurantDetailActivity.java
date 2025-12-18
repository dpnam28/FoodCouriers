package org.dpnam28.foodcouriers.ui.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.dpnam28.foodcouriers.R;
import org.dpnam28.foodcouriers.ui.fooddetail.FoodDetailActivity;
import org.dpnam28.foodcouriers.ui.menu.RestaurantMenuAdapter;
import org.dpnam28.foodcouriers.ui.menu.RestaurantMenuItem;
import org.dpnam28.foodcouriers.utils.ToastUtils;

import java.util.List;

public class RestaurantDetailActivity extends AppCompatActivity implements RestaurantDetailContract.View {

    public static final String EXTRA_RESTAURANT_ID = "extra_restaurant_id";

    private ListView listFoods;
    private ProgressBar progressBar;
    private RestaurantMenuAdapter adapter;

    private ImageView imgBanner;
    private TextView tvName;
    private TextView tvDescription;
    private TextView tvAddress;
    private TextView tvPhone;
    private TextView tvLocation;
    private TextView tvDeliveryFee;
    private TextView tvNoFoods;

    private RestaurantDetailContract.Presenter presenter;
    private long restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_detail);

        restaurantId = getIntent().getLongExtra(EXTRA_RESTAURANT_ID, 0L);
        if (restaurantId == 0L) {
            ToastUtils.showTopToast(this, "Không xác định được nhà hàng", ToastUtils.TYPE_ERROR);
            finish();
            return;
        }

        assignViews();
        presenter = new RestaurantDetailPresenter(this, this);
        presenter.loadRestaurant(restaurantId);
    }

    private void assignViews() {
        listFoods = findViewById(R.id.listRestaurantFoods);
        progressBar = findViewById(R.id.progressRestaurantDetail);
        adapter = new RestaurantMenuAdapter(this);

        View header = getLayoutInflater().inflate(R.layout.layout_restaurant_header, listFoods, false);
        imgBanner = header.findViewById(R.id.imgRestaurantBanner);
        tvName = header.findViewById(R.id.tvRestaurantName);
        tvDescription = header.findViewById(R.id.tvRestaurantDescription);
        tvAddress = header.findViewById(R.id.tvRestaurantAddress);
        tvPhone = header.findViewById(R.id.tvRestaurantPhone);
        tvLocation = header.findViewById(R.id.tvRestaurantLocation);
        tvDeliveryFee = header.findViewById(R.id.tvRestaurantDeliveryFee);
        tvNoFoods = header.findViewById(R.id.tvRestaurantNoFoods);
        ImageButton btnBack = header.findViewById(R.id.btnBackRestaurant);
        btnBack.setOnClickListener(v -> finish());

        listFoods.addHeaderView(header, null, false);
        listFoods.setAdapter(adapter);
        listFoods.setOnItemClickListener((parent, view, position, id) -> {
            int headerCount = listFoods.getHeaderViewsCount();
            if (position < headerCount) {
                return;
            }
            int dataIndex = position - headerCount;
            if (dataIndex < 0 || dataIndex >= adapter.getCount()) {
                return;
            }
            RestaurantMenuItem item = adapter.getItem(dataIndex);
            if (item == null) {
                return;
            }
            Intent intent = new Intent(this, FoodDetailActivity.class);
            intent.putExtra(FoodDetailActivity.EXTRA_FOOD_ID, item.getId());
            startActivity(intent);
        });
    }

    @Override
    public void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showRestaurantInfo(RestaurantDetailContract.RestaurantInfo info) {
        tvName.setText(info.getName());
        if (TextUtils.isEmpty(info.getDescription())) {
            tvDescription.setVisibility(View.GONE);
        } else {
            tvDescription.setVisibility(View.VISIBLE);
            tvDescription.setText(info.getDescription());
        }
        tvAddress.setText(getString(R.string.restaurant_address_label, safeValue(info.getAddress())));
        tvPhone.setText(getString(R.string.restaurant_phone_label, safeValue(info.getPhone())));
        tvLocation.setText(getString(R.string.restaurant_location_label, safeValue(info.getLocation())));
        tvDeliveryFee.setText(getString(R.string.restaurant_delivery_fee_label, safeValue(info.getDeliveryFee())));

        Glide.with(this)
                .load(info.getImageUrl())
                .centerCrop()
                .into(imgBanner);
    }

    @Override
    public void showFoods(List<RestaurantMenuItem> foods) {
        adapter.setItems(foods);
    }

    @Override
    public void showNoFoodsMessage(boolean show) {
        tvNoFoods.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private String safeValue(String value) {
        return TextUtils.isEmpty(value) ? "-" : value;
    }

    @Override
    public void showError(String message) {
        ToastUtils.showTopToast(this, TextUtils.isEmpty(message) ? "Đã xảy ra lỗi" : message, ToastUtils.TYPE_ERROR);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detach();
        }
    }
}
