package org.dpnam28.foodcouriers.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.dpnam28.foodcouriers.R;
import org.dpnam28.foodcouriers.ui.cart.CartActivity;
import org.dpnam28.foodcouriers.ui.fooddetail.FoodDetailActivity;
import org.dpnam28.foodcouriers.ui.menu.RestaurantMenuActivity;
import org.dpnam28.foodcouriers.ui.order.OrderActivity;
import org.dpnam28.foodcouriers.ui.profile.ProfileActivity;
import org.dpnam28.foodcouriers.ui.search.SearchActivity;
import org.dpnam28.foodcouriers.utils.ToastUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryClickListener, FoodAdapter.OnFoodClickListener, MainContract.View {

    private LinearLayout navHome, navCart, navProfile, navMenu, navHistory;
    private NestedScrollView scrollContentMain;
    private RecyclerView rvCategories;
    private RecyclerView rvFoods;
    private ProgressBar progressFoods;
    private TextView tvEmptyFoods;
    private EditText edtSearch;
    private ImageView iconSearch;
    private TextView txtAddress;
    private String userRole;
    private CategoryAdapter categoryAdapter;
    private FoodAdapter foodAdapter;
    private MainContract.Presenter presenter;
    private long selectedCategoryId = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        assignView();
        setupRecyclerViews();

        userRole = getSharedPreferences("userInfo", MODE_PRIVATE).getString("role", "");
        txtAddress.setText(String.format("%s, %s",
                getSharedPreferences("userInfo", MODE_PRIVATE).getString("address", ""),
                getSharedPreferences("userInfo", MODE_PRIVATE).getString("location", "")));
        setupBottomNavForRole();
        setupBottomNavigationActions();
        presenter = new MainPresenter(this, this);
        presenter.loadInitialData();
    }

    private void assignView() {
        navHome = findViewById(R.id.navHome);
        navCart = findViewById(R.id.navCart);
        navProfile = findViewById(R.id.navProfile);
        navMenu = findViewById(R.id.navMenu);
        navHistory = findViewById(R.id.navHistory);
        scrollContentMain = findViewById(R.id.scrollContentMain);
        rvCategories = findViewById(R.id.rvCategories);
        rvFoods = findViewById(R.id.rvFoods);
        progressFoods = findViewById(R.id.progressFoods);
        tvEmptyFoods = findViewById(R.id.tvEmptyFoods);
        edtSearch = findViewById(R.id.txtSearchHint);
        iconSearch = findViewById(R.id.icSearch);
        txtAddress = findViewById(R.id.txtAddress);
    }

    private void setupRecyclerViews() {
        categoryAdapter = new CategoryAdapter(this);
        rvCategories.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvCategories.setAdapter(categoryAdapter);
        rvCategories.setNestedScrollingEnabled(false);

        foodAdapter = new FoodAdapter(this);
        rvFoods.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvFoods.setAdapter(foodAdapter);
        rvFoods.setNestedScrollingEnabled(false);
    }

    private void setupBottomNavigationActions() {
        navHome.setOnClickListener(v -> scrollContentMain.smoothScrollTo(0, 0));
        navCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        navProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        navMenu.setOnClickListener(v -> {
            if ("ROLE_RESTAURANT".equals(userRole)) {
                startActivity(new Intent(this, RestaurantMenuActivity.class));
            } else {
                ToastUtils.showTopToast(this, getString(R.string.not_authorized_restaurant), ToastUtils.TYPE_ERROR);
            }
        });
        navHistory.setOnClickListener(v -> startActivity(new Intent(this, OrderActivity.class)));
        View.OnClickListener launchSearch = v -> openSearchActivity();
        iconSearch.setOnClickListener(launchSearch);
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (actionId == EditorInfo.IME_NULL && event != null && event.getAction() == KeyEvent.ACTION_DOWN)) {
                openSearchActivity();
                return true;
            }
            return false;
        });
    }

    private void setupBottomNavForRole() {
        navHistory.setVisibility(View.VISIBLE);
        if ("ROLE_RESTAURANT".equals(userRole)) {
            navCart.setVisibility(View.GONE);
            navMenu.setVisibility(View.VISIBLE);
        } else {
            navCart.setVisibility(View.VISIBLE);
            navMenu.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCategoryClicked(CategoryItem item) {
        if (item != null) {
            updateCategorySelection(item.getId());
            presenter.selectCategory(item.getId());
        }
    }

    @Override
    public void onFoodClicked(FoodItem item) {
        if (item == null) return;
        Intent intent = new Intent(this, FoodDetailActivity.class);
        intent.putExtra(FoodDetailActivity.EXTRA_FOOD_ID, item.getId());
        startActivity(intent);
    }

    private void openSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(SearchActivity.EXTRA_INITIAL_QUERY, edtSearch.getText().toString().trim());
        startActivity(intent);
    }

    private void updateCategorySelection(long categoryId) {
        if (categoryId == selectedCategoryId) return;
        selectedCategoryId = categoryId;
        categoryAdapter.setSelectedCategory(categoryId);
    }

    @Override
    public void showCategories(List<CategoryItem> categories, long selectedCategory) {
        categoryAdapter.setItems(categories);
        if (selectedCategory > 0) {
            updateCategorySelection(selectedCategory);
        }
    }

    @Override
    public void showFoods(List<FoodItem> foods) {
        foodAdapter.setItems(foods);
    }

    @Override
    public void showFoodsLoading(boolean loading) {
        progressFoods.setVisibility(loading ? View.VISIBLE : View.GONE);
        rvFoods.setVisibility(loading ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void showEmptyFoods(boolean isEmpty) {
        tvEmptyFoods.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String message) {
        ToastUtils.showTopToast(this, message == null ? "Đã xảy ra lỗi" : message, ToastUtils.TYPE_ERROR);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detach();
        }
    }
}
