package org.dpnam28.foodcouriers.ui.profile;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.dpnam28.foodcouriers.R;
import org.dpnam28.foodcouriers.utils.ToastUtils;

public class ProfileEditActivity extends AppCompatActivity implements ProfileContract.View {

    private EditText edtFullName;
    private EditText edtPhone;
    private EditText edtAddress;
    private EditText edtPassword;
    private EditText edtDescription;
    private EditText edtBannerImage;
    private EditText edtDeliveryFee;
    private View layoutRestaurantFields;
    private ProgressBar progressBar;
    private Button btnSave;
    private TextView titleName;
    private ProfileContract.Presenter presenter;
    private long userId;
    private long locationId;
    private String currentRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_edit);
        assignViews();

        presenter = new ProfilePresenter(this, this);
        userId = readUserId();
        if (userId == 0L) {
            ToastUtils.showTopToast(this, "Không tìm thấy thông tin người dùng", ToastUtils.TYPE_ERROR);
            finish();
            return;
        }

        presenter.getUser(userId);

        ImageButton btnBack = findViewById(R.id.btnBackEdit);
        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> submitForm());
    }

    private void assignViews() {
        edtFullName = findViewById(R.id.edtFullNameEdit);
        edtPhone = findViewById(R.id.edtPhoneEdit);
        edtAddress = findViewById(R.id.edtAddressEdit);
        edtPassword = findViewById(R.id.edtPasswordEdit);
        edtDescription = findViewById(R.id.edtDescriptionEdit);
        edtBannerImage = findViewById(R.id.edtBannerEdit);
        edtDeliveryFee = findViewById(R.id.edtDeliveryFeeEdit);
        layoutRestaurantFields = findViewById(R.id.layoutRestaurantFields);
        btnSave = findViewById(R.id.btnSaveProfile);
        progressBar = findViewById(R.id.progressProfileEdit);
        titleName = findViewById(R.id.titleName);
    }

    private long readUserId() {
        SharedPreferences sharedPreferences = getUserPrefs();
        String rawId = sharedPreferences.getString("id", "");
        if (TextUtils.isEmpty(rawId)) {
            return 0L;
        }
        try {
            return Long.parseLong(rawId);
        } catch (NumberFormatException ignored) {
            return 0L;
        }
    }

    private void submitForm() {
        String fullName = edtFullName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(fullName) ||
                TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(address) ||
                TextUtils.isEmpty(password)) {
            ToastUtils.showTopToast(this, "Vui lòng nhập đầy đủ thông tin bắt buộc", ToastUtils.TYPE_ERROR);
            return;
        }

        String description = null;
        String banner = null;
        Double deliveryFee = null;
        if ("ROLE_RESTAURANT".equals(currentRole)) {
            description = edtDescription.getText().toString().trim();
            banner = edtBannerImage.getText().toString().trim();
            String feeText = edtDeliveryFee.getText().toString().trim();
            if (TextUtils.isEmpty(feeText)) {
                ToastUtils.showTopToast(this, "Vui lòng nhập phí giao hàng", ToastUtils.TYPE_ERROR);
                return;
            }
            try {
                deliveryFee = Double.parseDouble(feeText);
            } catch (NumberFormatException ex) {
                ToastUtils.showTopToast(this, "Phí giao hàng không hợp lệ", ToastUtils.TYPE_ERROR);
                return;
            }
        }

        ProfileContract.UpdateForm form = new ProfileContract.UpdateForm(
                userId,
                password,
                fullName,
                phone,
                address,
                currentRole,
                description,
                banner,
                deliveryFee
        );

        presenter.updateUser(form);
    }

    @Override
    public void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnSave.setEnabled(!isLoading);
        btnSave.setAlpha(isLoading ? 0.6f : 1f);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUserLoaded(ProfileContract.UserDetail detail) {
        currentRole = detail.getRole();
        if (detail.getLocationId() != 0L) {
            locationId = detail.getLocationId();
            storeLocationId(locationId);
        } else {
            locationId = readStoredLocationId();
        }
        edtFullName.setText(detail.getFullName());
        edtPhone.setText(detail.getPhoneNumber());
        edtAddress.setText(detail.getAddress());
        edtPassword.setText("");

        if ("ROLE_RESTAURANT".equals(detail.getRole())) {
            titleName.setText("Tên nhà hàng");
            layoutRestaurantFields.setVisibility(View.VISIBLE);
            edtDescription.setText(detail.getDescription());
            edtBannerImage.setText(detail.getBannerImage());
            edtDeliveryFee.setText(detail.getDeliveryFee() == null ? "" : String.valueOf(detail.getDeliveryFee()));
        } else {
            layoutRestaurantFields.setVisibility(View.GONE);
            edtDescription.setText("");
            edtBannerImage.setText("");
            edtDeliveryFee.setText("");
        }
    }

    @Override
    public void onUpdateSuccess(ProfileContract.UserDetail detail) {
        saveProfileToPrefs(detail);
        ToastUtils.showTopToast(this, "Cập nhật thông tin thành công", ToastUtils.TYPE_SUCCESS);
        setResult(RESULT_OK);
        finish();
    }

    private void saveProfileToPrefs(ProfileContract.UserDetail detail) {
        SharedPreferences sharedPreferences = getUserPrefs();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fullName", detail.getFullName());
        editor.putString("phoneNumber", detail.getPhoneNumber());
        editor.putString("address", detail.getAddress());
        editor.putString("email", detail.getEmail());
        if (detail.getLocationName() != null) {
            editor.putString("location", detail.getLocationName());
        }
        long latestLocationId = locationId != 0L ? locationId : detail.getLocationId();
        if (latestLocationId != 0L) {
            editor.putLong("locationId", latestLocationId);
        }
        editor.apply();
    }

    @Override
    public void onError(String message) {
        ToastUtils.showTopToast(this, message, ToastUtils.TYPE_ERROR);
    }

    private long readStoredLocationId() {
        return getUserPrefs().getLong("locationId", 0L);
    }

    private void storeLocationId(long value) {
        if (value == 0L) return;
        SharedPreferences.Editor editor = getUserPrefs().edit();
        editor.putLong("locationId", value);
        editor.apply();
    }

    private SharedPreferences getUserPrefs() {
        return getSharedPreferences("userInfo", MODE_PRIVATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detach();
        }
    }
}
