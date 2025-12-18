package org.dpnam28.foodcouriers.ui.order;

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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import org.dpnam28.foodcouriers.R;
import org.dpnam28.foodcouriers.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity implements OrderContract.View, OrderAdapter.OrderActionListener {

    private ImageButton btnBack;
    private ListView listOrders;
    private TextView tvEmpty;
    private ProgressBar progressBar;

    private OrderAdapter adapter;
    private OrderContract.Presenter presenter;
    private long userId;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);
        assignView();
        initUserInfo();

        adapter = new OrderAdapter(this, userRole);
        adapter.setActionListener(this);
        listOrders.setAdapter(adapter);

        presenter = new OrderPresenter(this, this);

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userId != 0L) {
            presenter.loadOrders(userId, userRole);
        } else {
            ToastUtils.showTopToast(this, "Không xác định được người dùng", ToastUtils.TYPE_ERROR);
        }
    }

    private void assignView() {
        btnBack = findViewById(R.id.btnBack);
        listOrders = findViewById(R.id.listOrders);
        tvEmpty = findViewById(R.id.tvEmptyOrders);
        progressBar = findViewById(R.id.progressOrders);
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
    }

    @Override
    public void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onOrdersLoaded(List<OrderModel> orders) {
        if (orders == null) {
            orders = new ArrayList<>();
        } else {
            orders.sort((o1, o2) -> Long.compare(o2.getId(), o1.getId()));
        }
        adapter.setItems(orders);
        boolean isEmpty = orders.isEmpty();
        listOrders.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        tvEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onOrderActionSuccess(String message) {
        if (TextUtils.isEmpty(message)) {
            message = getString(R.string.order_action_success);
        }
        ToastUtils.showTopToast(this, message, ToastUtils.TYPE_SUCCESS);
        presenter.loadOrders(userId, userRole);
    }

    @Override
    public void showError(String message) {
        if (TextUtils.isEmpty(message)) {
            message = "Đã xảy ra lỗi";
        }
        ToastUtils.showTopToast(this, message, ToastUtils.TYPE_ERROR);
    }

    @Override
    public void onCancelOrder(OrderModel order) {
        showConfirmationDialog(
                getString(R.string.order_cancel_confirm),
                getString(R.string.order_cancel),
                () -> presenter.cancelOrder(order.getId(), userId, userRole)
        );
    }

    @Override
    public void onAcceptOrder(OrderModel order) {
        if ("ROLE_RESTAURANT".equals(userRole)) {
            openSelectCourier(order.getId());
            return;
        }
        showConfirmationDialog(
                getString(R.string.order_accept_confirm),
                getString(R.string.order_accept),
                () -> presenter.acceptOrder(order.getId(), userId)
        );
    }

    @Override
    public void onMarkDelivered(OrderModel order) {
        showConfirmationDialog(
                getString(R.string.order_deliver_confirm),
                getString(R.string.order_mark_delivered),
                () -> presenter.deliverOrder(order.getId(), userId)
        );
    }

    private void openSelectCourier(long orderId) {
        Intent intent = new Intent(this, SelectCourierActivity.class);
        intent.putExtra(SelectCourierActivity.EXTRA_ORDER_ID, orderId);
        startActivity(intent);
    }

    private void showConfirmationDialog(String message, String confirmLabel, Runnable confirmAction) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_cart_confirm, null);
        TextView tvMessage = dialogView.findViewById(R.id.tvDialogMessage);
        AppCompatButton btnConfirm = dialogView.findViewById(R.id.btnDialogConfirm);
        tvMessage.setText(message);
        btnConfirm.setText(confirmLabel);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();
        dialogView.findViewById(R.id.btnDialogCancel).setOnClickListener(v -> dialog.dismiss());
        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            if (confirmAction != null) {
                confirmAction.run();
            }
        });
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detach();
        }
    }
}
