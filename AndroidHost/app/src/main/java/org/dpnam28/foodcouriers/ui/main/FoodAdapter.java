package org.dpnam28.foodcouriers.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.dpnam28.foodcouriers.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    public interface OnFoodClickListener {
        void onFoodClicked(FoodItem item);
    }

    private final List<FoodItem> items = new ArrayList<>();
    private final OnFoodClickListener listener;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public FoodAdapter(OnFoodClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<FoodItem> foods) {
        items.clear();
        if (foods != null) {
            items.addAll(foods);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_card, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem item = items.get(position);
        holder.bind(item);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFoodClicked(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgFood;
        private final TextView tvName;
        private final TextView tvDescription;
        private final TextView tvPrice;

        FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            tvName = itemView.findViewById(R.id.tvFoodName);
            tvDescription = itemView.findViewById(R.id.tvFoodDescription);
            tvPrice = itemView.findViewById(R.id.tvFoodPrice);
        }

        void bind(FoodItem item) {
            tvName.setText(item.getName());
            tvDescription.setText(item.getDescription());
            tvPrice.setText(currencyFormat.format(item.getPrice()));
            Glide.with(imgFood.getContext())
                    .load(item.getImageUrl())
                    .centerCrop()
                    .placeholder(R.drawable.bg_button_secondary)
                    .into(imgFood);
        }
    }
}
