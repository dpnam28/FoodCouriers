package org.dpnam28.foodcouriers.ui.menu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import org.dpnam28.foodcouriers.R;
import org.dpnam28.foodcouriers.ui.foodeditor.FoodEditorActivity;
import org.dpnam28.foodcouriers.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RestaurantMenuActivity extends AppCompatActivity implements RestaurantMenuContract.View {

    private EditText edtSearch;
    private ListView listFoods;
    private View emptyView;
    private ProgressBar progressBar;
    private RestaurantMenuContract.Presenter presenter;
    private RestaurantMenuAdapter adapter;
    private final List<RestaurantMenuItem> allFoods = new ArrayList<>();
    private String role;
    private long userId;

    private final ActivityResultLauncher<Intent> editorLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    presenter.getFoods(userId);
                }
            });
    private final static String TAG = "RestaurantMenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_restaurant);

        SharedPreferences prefs = getSharedPreferences("userInfo", MODE_PRIVATE);
        role = prefs.getString("role", "");
        userId = parseLongSafe(prefs.getString("id", ""));


        if (!"ROLE_RESTAURANT".equals(role) || userId == 0L) {
            ToastUtils.showTopToast(this, getString(R.string.not_authorized_restaurant), ToastUtils.TYPE_ERROR);
            finish();
            return;
        }

        presenter = new RestaurantPresenter(this, this);
        assignViews();
        setupList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getFoods(userId);
    }

    private void assignViews() {
        edtSearch = findViewById(R.id.edtSearchFood);
        listFoods = findViewById(R.id.listFoods);
        emptyView = findViewById(R.id.layoutEmptyState);
        progressBar = findViewById(R.id.progressMenu);
        ImageButton btnBack = findViewById(R.id.btnBackMenu);
        btnBack.setOnClickListener(v -> finish());
        ImageButton btnAddFood = findViewById(R.id.btnAddFood);
        btnAddFood.setOnClickListener(v -> openFoodEditor(0L));
    }

    private void setupList() {
        adapter = new RestaurantMenuAdapter(this);
        listFoods.setAdapter(adapter);
        listFoods.setOnItemClickListener((parent, view, position, id) -> {
            RestaurantMenuItem item = adapter.getItem(position);
            openFoodEditor(item.getId());
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFoods(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }


    private void filterFoods(String query) {
        if (query == null || query.trim().isEmpty()) {
            updateList(allFoods);
            return;
        }
        String lower = query.toLowerCase(Locale.getDefault());
        List<RestaurantMenuItem> filtered = new ArrayList<>();
        for (RestaurantMenuItem item : allFoods) {
            if (item.getName().toLowerCase(Locale.getDefault()).contains(lower)) {
                filtered.add(item);
            }
        }
        updateList(filtered);
    }

    private void updateList(List<RestaurantMenuItem> data) {
        adapter.setItems(data);
        if (data.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            listFoods.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            listFoods.setVisibility(View.VISIBLE);
        }
    }

    private long parseLongSafe(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    public void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onFoodsLoaded(List<RestaurantMenuItem> foods) {
        allFoods.clear();
        if (foods != null) {
            allFoods.addAll(foods);
        }
        updateList(allFoods);
    }

    @Override
    public void onError(String message) {
        Log.e(TAG, "onError: " + message);
        ToastUtils.showTopToast(this, message, ToastUtils.TYPE_ERROR);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detach();
        }
    }

    private void openFoodEditor(long foodId) {
        Intent intent = new Intent(this, FoodEditorActivity.class);
        intent.putExtra(FoodEditorActivity.EXTRA_FOOD_ID, foodId);
        editorLauncher.launch(intent);
    }
}
