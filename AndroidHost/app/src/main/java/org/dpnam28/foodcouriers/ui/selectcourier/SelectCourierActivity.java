package org.dpnam28.foodcouriers.ui.selectcourier;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.dpnam28.foodcouriers.R;
import org.dpnam28.foodcouriers.ui.main.MainActivity;
import org.dpnam28.foodcouriers.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectCourierActivity extends AppCompatActivity implements SelectCourierContract.View, CourierAdapter.CourierSelectListener {

    public static final String EXTRA_ORDER_ID = "extra_order_id";

    private ListView listCouriers;
    private TextView tvEmpty;
    private ProgressBar progressBar;
    private CourierAdapter adapter;
    private ImageButton btnBack;
    private SelectCourierContract.Presenter presenter;
    private long orderId;
    private long restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_courier);
        assignViews();

        SharedPreferences prefs = getSharedPreferences("userInfo", MODE_PRIVATE);
        restaurantId = parseLong(prefs.getString("id", ""));
        String location = prefs.getString("location", "");

        orderId = getIntent().getLongExtra(EXTRA_ORDER_ID, 0L);
        if (orderId == 0L || restaurantId == 0L) {
            ToastUtils.showTopToast(this, "Không xác định được đơn hàng", ToastUtils.TYPE_ERROR);
            finish();
            return;
        }
        btnBack.setOnClickListener(v -> finish());
        presenter = new SelectCourierPresenter(this, this);
        presenter.loadCouriers(location);
    }

    private void assignViews() {
        btnBack = findViewById(R.id.btnBack);
        listCouriers = findViewById(R.id.listCouriers);
        tvEmpty = findViewById(R.id.tvEmpty);
        progressBar = findViewById(R.id.progressCouriers);
        adapter = new CourierAdapter(LayoutInflater.from(this));
        adapter.setCourierSelectListener(this);
        listCouriers.setAdapter(adapter);
    }

    private long parseLong(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0L;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    @Override
    public void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showCouriers(List<SelectCourierContract.CourierItem> couriers) {
        if (couriers == null) {
            couriers = new ArrayList<>();
        }
        adapter.setCouriers(couriers);
        boolean isEmpty = couriers.isEmpty();
        listCouriers.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        tvEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String message) {
        if (TextUtils.isEmpty(message)) {
            message = "Đã xảy ra lỗi";
        }
        ToastUtils.showTopToast(this, message, ToastUtils.TYPE_ERROR);
    }

    @Override
    public void onAssignmentSuccess(String message) {
        ToastUtils.showTopToast(this,
                TextUtils.isEmpty(message) ? getString(R.string.courier_assign_success) : message,
                ToastUtils.TYPE_SUCCESS);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCourierSelected(SelectCourierContract.CourierItem courier) {
        presenter.assignCourier(orderId, restaurantId, courier.getId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detach();
        }
    }
}
