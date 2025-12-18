package org.dpnam28.foodcouriers.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.dpnam28.foodcouriers.R;
import org.dpnam28.foodcouriers.ui.fooddetail.FoodDetailActivity;
import org.dpnam28.foodcouriers.ui.restaurant.RestaurantDetailActivity;
import org.dpnam28.foodcouriers.utils.ToastUtils;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchContract.View {

    public static final String EXTRA_INITIAL_QUERY = "extra_initial_query";

    private Spinner spinnerType;
    private EditText edtKeyword;
    private ImageView btnSearch;
    private ListView listResults;
    private TextView tvEmpty;
    private ProgressBar progressBar;

    private SearchResultAdapter adapter;
    private SearchContract.Presenter presenter;
    private SearchContract.SearchType currentType = SearchContract.SearchType.FOOD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        presenter = new SearchPresenter(this, this);
        assignViews();
        setupSpinner();
        setupList();
        setupSearchHandlers();

        String initialQuery = getIntent().getStringExtra(EXTRA_INITIAL_QUERY);
        if (!TextUtils.isEmpty(initialQuery)) {
            edtKeyword.setText(initialQuery);
            edtKeyword.setSelection(initialQuery.length());
            performSearch();
        }
    }

    private void assignViews() {
        spinnerType = findViewById(R.id.spinnerSearchType);
        edtKeyword = findViewById(R.id.edtKeyword);
        btnSearch = findViewById(R.id.btnSearchAction);
        listResults = findViewById(R.id.listSearchResults);
        tvEmpty = findViewById(R.id.tvEmptySearch);
        progressBar = findViewById(R.id.progressSearch);
        adapter = new SearchResultAdapter(this);
        listResults.setAdapter(adapter);

        ImageButton btnBack = findViewById(R.id.btnBackSearch);
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.search_type_entries,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(spinnerAdapter);
        spinnerType.setSelection(0);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentType = position == 0 ? SearchContract.SearchType.FOOD : SearchContract.SearchType.RESTAURANT;
                tvEmpty.setVisibility(View.GONE);
                adapter.setItems(null, currentType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void setupList() {
        listResults.setOnItemClickListener((parent, view, position, id) -> {
            SearchResultItem item = adapter.getItem(position);
            if (item == null) return;
            if (currentType == SearchContract.SearchType.FOOD) {
                Intent intent = new Intent(this, FoodDetailActivity.class);
                intent.putExtra(FoodDetailActivity.EXTRA_FOOD_ID, item.getId());
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, RestaurantDetailActivity.class);
                intent.putExtra(RestaurantDetailActivity.EXTRA_RESTAURANT_ID, item.getId());
                startActivity(intent);
            }
        });
    }

    private void setupSearchHandlers() {
        btnSearch.setOnClickListener(v -> performSearch());
        edtKeyword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (actionId == EditorInfo.IME_NULL && event != null && event.getAction() == KeyEvent.ACTION_DOWN)) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    private void performSearch() {
        String keyword = edtKeyword.getText().toString().trim();
        tvEmpty.setVisibility(View.GONE);
        presenter.search(keyword, currentType);
    }

    @Override
    public void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showResults(List<SearchResultItem> results, SearchContract.SearchType type) {
        adapter.setItems(results, type);
        tvEmpty.setVisibility(results == null || results.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showEmptyState() {
        adapter.setItems(null, currentType);
        tvEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String message) {
        ToastUtils.showTopToast(this, TextUtils.isEmpty(message) ? "Đã xảy ra lỗi" : message, ToastUtils.TYPE_ERROR);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }
}
