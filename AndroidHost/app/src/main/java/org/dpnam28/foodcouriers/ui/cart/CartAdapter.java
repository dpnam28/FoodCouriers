package org.dpnam28.foodcouriers.ui.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import org.dpnam28.foodcouriers.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends BaseAdapter {

    public interface CartActionListener {
        void onEditQuantity(CartItemModel item);

        void onDeleteItem(CartItemModel item);
    }

    private final List<CartItemModel> items = new ArrayList<>();
    private final LayoutInflater inflater;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private CartActionListener actionListener;

    public CartAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void setItems(List<CartItemModel> data) {
        items.clear();
        if (data != null) {
            items.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void setCartActionListener(CartActionListener listener) {
        this.actionListener = listener;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CartItemModel getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_cart_entry, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CartItemModel item = getItem(position);
        holder.tvName.setText(item.getFoodName());
        holder.tvQuantity.setText(parent.getContext().getString(R.string.cart_quantity_format, item.getQuantity()));
        holder.tvPrice.setText(currencyFormat.format(item.getTotalPrice()));
        holder.btnEdit.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onEditQuantity(item);
            }
        });
        holder.btnDelete.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDeleteItem(item);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView tvName;
        TextView tvQuantity;
        TextView tvPrice;
        AppCompatButton btnEdit;
        AppCompatButton btnDelete;

        ViewHolder(View view) {
            tvName = view.findViewById(R.id.tvCartFoodName);
            tvQuantity = view.findViewById(R.id.tvCartQuantity);
            tvPrice = view.findViewById(R.id.tvCartPrice);
            btnEdit = view.findViewById(R.id.btnEditQuantity);
            btnDelete = view.findViewById(R.id.btnDeleteItem);
        }
    }
}
