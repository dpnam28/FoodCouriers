package org.dpnam28.foodcouriers.ui.profile;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.dpnam28.foodcouriers.R;
import org.dpnam28.foodcouriers.utils.ToastUtils;

public class ProfileEditActivity extends AppCompatActivity implements ProfileContract.View {

    private EditText edtFullName;
    private EditText edtPhone;
    private EditText edtAddress;
    private EditText edtPassword;
    private EditText edtDescription;
    private ImageView imgBanner;
    private View layoutRestaurantFields;
    private ProgressBar progressBar;
    private Button btnSave;
    private Button btnChooseImage;
    private TextView titleName;
    private ProfileContract.Presenter presenter;
    private long userId;
    private String currentRole;
    private Uri selectedBannerUri;
    private String currentBannerUrl;
    private ActivityResultLauncher<String> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_edit);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedBannerUri = uri;
                        currentBannerUrl = null;
                        displayBanner(uri);
                    }
                }
        );

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
        btnChooseImage.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));
    }

    private void assignViews() {
        edtFullName = findViewById(R.id.edtFullNameEdit);
        edtPhone = findViewById(R.id.edtPhoneEdit);
        edtAddress = findViewById(R.id.edtAddressEdit);
        edtPassword = findViewById(R.id.edtPasswordEdit);
        edtDescription = findViewById(R.id.edtDescriptionEdit);
        imgBanner = findViewById(R.id.imgBanner);
        layoutRestaurantFields = findViewById(R.id.layoutRestaurantFields);
        btnSave = findViewById(R.id.btnSaveProfile);
        btnChooseImage = findViewById(R.id.btnChooseImage);
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
                TextUtils.isEmpty(address)){
            ToastUtils.showTopToast(this, "Vui lòng nhập đầy đủ thông tin bắt buộc", ToastUtils.TYPE_ERROR);
            return;
        }

        if(TextUtils.isEmpty(password)) {
            ToastUtils.showTopToast(this, "Vui lòng nhập mật khẩu", ToastUtils.TYPE_ERROR);
            return;
        }

        String description = "";
        if ("ROLE_RESTAURANT".equals(currentRole)) {
            description = edtDescription.getText().toString().trim();
        }

        boolean isRestaurant = "ROLE_RESTAURANT".equals(currentRole);

        ProfileContract.UpdateForm form = new ProfileContract.UpdateForm(
                userId,
                password,
                fullName,
                phone,
                address,
                isRestaurant,
                description
        );

        presenter.updateUser(form, selectedBannerUri);
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
        edtFullName.setText(detail.getFullName());
        edtPhone.setText(detail.getPhoneNumber());
        edtAddress.setText(detail.getAddress());
        edtPassword.setText("");

        currentBannerUrl = detail.getBannerImage();
        selectedBannerUri = null;
        displayBanner(currentBannerUrl);

        if ("ROLE_RESTAURANT".equals(detail.getRole())) {
            titleName.setText("Tên nhà hàng");
            layoutRestaurantFields.setVisibility(View.VISIBLE);
            edtDescription.setText(detail.getDescription());
        } else {
            layoutRestaurantFields.setVisibility(View.GONE);
            edtDescription.setText("");
        }
    }

    @Override
    public void onUpdateSuccess(ProfileContract.UserDetail detail) {
        saveProfileToPrefs(detail);
        ToastUtils.showTopToast(this, "Cập nhật thông tin thành công", ToastUtils.TYPE_SUCCESS);
        setResult(RESULT_OK);
        currentBannerUrl = detail.getBannerImage();
        selectedBannerUri = null;
        displayBanner(currentBannerUrl);
        finish();
    }

    private void saveProfileToPrefs(ProfileContract.UserDetail detail) {
        SharedPreferences sharedPreferences = getUserPrefs();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fullName", detail.getFullName());
        editor.putString("phoneNumber", detail.getPhoneNumber());
        editor.putString("address", detail.getAddress());
        editor.putString("email", detail.getEmail());
        editor.apply();
    }

    private void displayBanner(Object source) {
        if (imgBanner == null) return;
        Glide.with(this)
                .load(source)
                .centerCrop()
                .into(imgBanner);
    }

    @Override
    public void onError(String message) {
        ToastUtils.showTopToast(this, message, ToastUtils.TYPE_ERROR);
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
