package org.dpnam28.foodcouriers.ui.foodeditor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.dpnam28.foodcouriers.R;
import org.dpnam28.foodcouriers.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class FoodEditorActivity extends AppCompatActivity implements FoodEditorContract.View {

    public static final String EXTRA_FOOD_ID = "extra_food_id";

    private EditText edtName;
    private EditText edtDescription;
    private EditText edtPrice;
    private Spinner spinnerCategory;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchActive;
    private TextView foodStatus;
    private ImageView imgPreview;
    private TextView btnChooseImage;
    private TextView tvTitle;
    private ProgressBar progressBar;

    private FoodEditorContract.Presenter presenter;
    private Uri selectedImageUri;
    private final List<FoodEditorContract.CategoryOption> categories = new ArrayList<>();
    private ArrayAdapter<FoodEditorContract.CategoryOption> categoryAdapter;

    private long foodId;
    private boolean categoriesLoaded = false;
    private long pendingCategoryId = -1L;

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    displayImage(uri);
                }
            });

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_editor);

        assignViews();
        presenter = new FoodEditorPresenter(this, this);

        foodId = getIntent().getLongExtra(EXTRA_FOOD_ID, 0L);
        tvTitle.setText(foodId == 0L ? R.string.add_food : R.string.edit_food);

        ImageButton btnBack = findViewById(R.id.btnBackFoodEditor);
        btnBack.setOnClickListener(v -> finish());
        btnChooseImage.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));
        findViewById(R.id.btnSaveFood).setOnClickListener(v -> onSaveClicked());
        switchActive.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked) {
                foodStatus.setText("Đang bán");
            } else {
                foodStatus.setText("Ngừng bán");
            }
        });
        setupCategorySpinner();
        presenter.loadCategories();
        if (foodId != 0L) {
            presenter.loadFoodDetail(foodId);
        }
    }

    private void assignViews() {
        edtName = findViewById(R.id.edtFoodName);
        edtDescription = findViewById(R.id.edtFoodDescription);
        edtPrice = findViewById(R.id.edtFoodPrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        switchActive = findViewById(R.id.switchActive);
        imgPreview = findViewById(R.id.imgFoodPreview);
        btnChooseImage = findViewById(R.id.btnChooseFoodImage);
        tvTitle = findViewById(R.id.tvFoodEditorTitle);
        progressBar = findViewById(R.id.progressFoodEditor);
        foodStatus = findViewById(R.id.foodStatus);
    }

    private void setupCategorySpinner() {
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(categoryAdapter);
    }

    private void onSaveClicked() {
        String name = edtName.getText().toString().trim();
        String priceText = edtPrice.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            ToastUtils.showTopToast(this, "Vui lòng nhập tên", ToastUtils.TYPE_ERROR);
            showLoading(false);
            return;
        }
        if(TextUtils.isEmpty(priceText)){
            ToastUtils.showTopToast(this, "Vui lòng nhập giá", ToastUtils.TYPE_ERROR);
            showLoading(false);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            ToastUtils.showTopToast(this, "Giá không hợp lệ", ToastUtils.TYPE_ERROR);
            showLoading(false);
            return;
        }

        if (spinnerCategory.getSelectedItem() == null) {
            ToastUtils.showTopToast(this, "Vui lòng chọn danh mục", ToastUtils.TYPE_ERROR);
            showLoading(false);
            return;
        }
        FoodEditorContract.CategoryOption option = (FoodEditorContract.CategoryOption) spinnerCategory.getSelectedItem();

        FoodEditorContract.Form form = new FoodEditorContract.Form(
                name,
                edtDescription.getText().toString().trim(),
                option.getId(),
                price,
                switchActive.isChecked()
        );

        if (foodId == 0L) {
            presenter.createFood(form, selectedImageUri);
        } else {
            presenter.updateFood(foodId, form, selectedImageUri);
        }
    }

    private void displayImage(Object source) {
        Glide.with(this)
                .load(source)
                .centerCrop()
                .into(imgPreview);
    }

    @Override
    public void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        findViewById(R.id.btnSaveFood).setEnabled(!loading);
    }

    @Override
    public void onCategoriesLoaded(List<FoodEditorContract.CategoryOption> options) {
        categories.clear();
        if (options != null) {
            categories.addAll(options);
        }
        categoryAdapter.notifyDataSetChanged();
        categoriesLoaded = true;
        if (pendingCategoryId != -1L) {
            selectCategory(pendingCategoryId);
            pendingCategoryId = -1L;
        }
    }

    @Override
    public void onFoodLoaded(FoodEditorContract.FoodDetail detail) {
        selectedImageUri = null;
        edtName.setText(detail.getName());
        edtDescription.setText(detail.getDescription());
        edtPrice.setText(String.valueOf(detail.getPrice()));
        switchActive.setChecked(detail.isActive());
        foodStatus.setText(detail.isActive() ? "Đang bán" : "Ngừng bán");
        if (categoriesLoaded) {
            selectCategory(detail.getCategoryId());
        } else {
            pendingCategoryId = detail.getCategoryId();
        }
        displayImage(detail.getImageUrl());
    }

    private void selectCategory(long categoryId) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId() == categoryId) {
                spinnerCategory.setSelection(i);
                break;
            }
        }
    }

    @Override
    public void onSaveSuccess() {
        ToastUtils.showTopToast(this, "Cập nhật thành công", ToastUtils.TYPE_SUCCESS);
        setResult(RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void onError(String message) {
        if (TextUtils.isEmpty(message)) {
            message = "Đã xảy ra lỗi";
        }
        ToastUtils.showTopToast(this, message, ToastUtils.TYPE_ERROR);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detach();
        }
    }
}
