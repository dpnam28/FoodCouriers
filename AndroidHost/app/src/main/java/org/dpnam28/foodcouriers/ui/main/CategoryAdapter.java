package org.dpnam28.foodcouriers.ui.main;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.dpnam28.foodcouriers.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClicked(CategoryItem item);
    }

    private final List<CategoryItem> items = new ArrayList<>();
    private final OnCategoryClickListener listener;
    private long selectedCategoryId = -1L;

    public CategoryAdapter(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setItems(List<CategoryItem> categories) {
        items.clear();
        if (categories != null) {
            items.addAll(categories);
        }
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSelectedCategory(long categoryId) {
        selectedCategoryId = categoryId;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_chip, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryItem item = items.get(position);
        holder.bind(item, item.getId() == selectedCategoryId);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryClicked(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCategoryName;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }

        void bind(CategoryItem item, boolean selected) {
            tvCategoryName.setText(item.getName());
            int background = selected ? R.drawable.bg_button_primary : R.drawable.bg_button_outline_primary;
            tvCategoryName.setBackgroundResource(background);
            int textColorRes = selected ? android.R.color.white : R.color.black;
            tvCategoryName.setTextColor(ContextCompat.getColor(tvCategoryName.getContext(), textColorRes));
        }
    }
}
