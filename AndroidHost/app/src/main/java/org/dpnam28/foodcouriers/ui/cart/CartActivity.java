package org.dpnam28.foodcouriers.ui.cart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.dpnam28.foodcouriers.FinishScreenActivity;
import org.dpnam28.foodcouriers.R;
import org.dpnam28.foodcouriers.utils.ToastUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartActionListener, CartContract.View {

    private ImageButton btnBack;
    private Button btnPlaceOrder;
    private ListView listCartItems;
    private TextView tvEmptyCart;
    private TextView tvDeliveryValue;
    private TextView tvSubTotalValue;
    private TextView tvTotalValue;
    private ProgressBar progressBar;

    private final List<CartItemModel> cartItems = new ArrayList<>();
    private CartAdapter adapter;
    private CartContract.Presenter presenter;
    private long userId;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        assignView();
        initUserInfo();

        presenter = new CartPresenter(this, this);

        btnBack.setOnClickListener(v -> finish());
        btnPlaceOrder.setOnClickListener(v -> startActivity(new Intent(this, FinishScreenActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userId != 0L) {
            presenter.loadCart(userId);
        } else {
            ToastUtils.showTopToast(this, "Không xác định được người dùng", ToastUtils.TYPE_ERROR);
        }
    }

    private void assignView() {
        btnBack = findViewById(R.id.btnBack);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        listCartItems = findViewById(R.id.listCartItems);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        tvDeliveryValue = findViewById(R.id.tvDeliveryValue);
        tvSubTotalValue = findViewById(R.id.tvSubTotalValue);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        progressBar = findViewById(R.id.progressCart);

        adapter = new CartAdapter(this);
        adapter.setCartActionListener(this);
        listCartItems.setAdapter(adapter);
    }

    private void initUserInfo() {
        SharedPreferences prefs = getSharedPreferences("userInfo", MODE_PRIVATE);
        String idValue = prefs.getString("id", "");
        try {
            userId = Long.parseLong(idValue);
        } catch (NumberFormatException e) {
            userId = 0L;
        }
    }

    private void updateSummary() {
        double subTotal = 0d;
        for (CartItemModel item : cartItems) {
            subTotal += item.getTotalPrice();
        }
        double delivery = 0d;
        double total = subTotal + delivery;
        tvSubTotalValue.setText(currencyFormat.format(subTotal));
        tvDeliveryValue.setText(currencyFormat.format(delivery));
        tvTotalValue.setText(currencyFormat.format(total));
    }

    private void toggleEmptyState() {
        boolean isEmpty = cartItems.isEmpty();
        tvEmptyCart.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        listCartItems.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        btnPlaceOrder.setEnabled(!isEmpty);
    }

    @Override
    public void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCartItemsLoaded(List<CartItemModel> items) {
        cartItems.clear();
        if (items != null) {
            cartItems.addAll(items);
        }
        adapter.setItems(cartItems);
        updateSummary();
        toggleEmptyState();
    }

    @Override
    public void onCartActionSuccess(String message) {
        ToastUtils.showTopToast(this, message, ToastUtils.TYPE_SUCCESS);
        presenter.loadCart(userId);
    }

    @Override
    public void showError(String message) {
        if (TextUtils.isEmpty(message)) {
            message = "Đã xảy ra lỗi";
        }
        ToastUtils.showTopToast(this, message, ToastUtils.TYPE_ERROR);
    }

    @Override
    public void onEditQuantity(CartItemModel item) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_update_quantity, null);
        EditText edtQuantity = dialogView.findViewById(R.id.edtQuantity);
        edtQuantity.setText(String.valueOf(item.getQuantity()));
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();
        dialogView.findViewById(R.id.btnDialogCancel).setOnClickListener(v -> dialog.dismiss());
        dialogView.findViewById(R.id.btnDialogConfirm).setOnClickListener(v -> {
            String text = edtQuantity.getText().toString().trim();
            if (TextUtils.isEmpty(text)) {
                edtQuantity.setError(getString(R.string.cart_quantity_error));
                return;
            }
            int qty;
            try {
                qty = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                edtQuantity.setError(getString(R.string.cart_quantity_error));
                return;
            }
            if (qty < 1) {
                edtQuantity.setError(getString(R.string.cart_quantity_error));
                return;
            }
            dialog.dismiss();
            presenter.updateCartItem(item.getId(), qty);
        });
        dialog.show();
    }

    @Override
    public void onDeleteItem(CartItemModel item) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_cart_confirm, null);
        TextView tvMessage = dialogView.findViewById(R.id.tvDialogMessage);
        tvMessage.setText(getString(R.string.cart_delete_confirm));
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();
        dialogView.findViewById(R.id.btnDialogCancel).setOnClickListener(v -> dialog.dismiss());
        dialogView.findViewById(R.id.btnDialogConfirm).setOnClickListener(v -> {
            dialog.dismiss();
            presenter.deleteCartItem(item.getId());
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
