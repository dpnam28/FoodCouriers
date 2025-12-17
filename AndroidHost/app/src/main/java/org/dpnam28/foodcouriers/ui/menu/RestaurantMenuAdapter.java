package org.dpnam28.foodcouriers.ui.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.dpnam28.foodcouriers.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RestaurantMenuAdapter extends BaseAdapter {

    private final Context context;
    private final LayoutInflater inflater;
    private final List<RestaurantMenuItem> items = new ArrayList<>();

    public RestaurantMenuAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setItems(List<RestaurantMenuItem> data) {
        items.clear();
        if (data != null) {
            items.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public RestaurantMenuItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_food_menu, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RestaurantMenuItem item = getItem(position);
        holder.tvName.setText(item.getName());
        holder.tvDesc.setText(item.getDescription());
        holder.tvPrice.setText(formatPrice(item.getPrice()));

        Glide.with(context)
                .load(item.getImageUrl())
                .centerCrop()
                .into(holder.imgFood);

        return convertView;
    }

    private String formatPrice(double price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(price);
    }

    private static class ViewHolder {
        ImageView imgFood;
        TextView tvName;
        TextView tvDesc;
        TextView tvPrice;

        ViewHolder(View view) {
            imgFood = view.findViewById(R.id.imgFood);
            tvName = view.findViewById(R.id.tvFoodName);
            tvDesc = view.findViewById(R.id.tvFoodDesc);
            tvPrice = view.findViewById(R.id.tvFoodPrice);
        }
    }
}
