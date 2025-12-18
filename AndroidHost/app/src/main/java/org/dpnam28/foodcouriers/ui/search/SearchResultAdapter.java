package org.dpnam28.foodcouriers.ui.search;

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

public class SearchResultAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final List<SearchResultItem> items = new ArrayList<>();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private SearchContract.SearchType currentType = SearchContract.SearchType.FOOD;

    public SearchResultAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void setItems(List<SearchResultItem> data, SearchContract.SearchType type) {
        items.clear();
        if (data != null) {
            items.addAll(data);
        }
        currentType = type;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public SearchResultItem getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_search_result, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SearchResultItem item = getItem(position);
        holder.tvName.setText(item.getName());
        holder.tvDescription.setText(item.getDescription());
        if (currentType == SearchContract.SearchType.FOOD && item.getPrice() != null) {
            holder.tvPrice.setVisibility(View.VISIBLE);
            holder.tvPrice.setText(currencyFormat.format(item.getPrice()));
        } else {
            holder.tvPrice.setVisibility(View.GONE);
        }
        Glide.with(holder.imgThumb.getContext())
                .load(item.getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.bg_button_secondary)
                .into(holder.imgThumb);

        return convertView;
    }

    static class ViewHolder {
        ImageView imgThumb;
        TextView tvName;
        TextView tvDescription;
        TextView tvPrice;

        ViewHolder(View view) {
            imgThumb = view.findViewById(R.id.imgResultThumb);
            tvName = view.findViewById(R.id.tvResultName);
            tvDescription = view.findViewById(R.id.tvResultDescription);
            tvPrice = view.findViewById(R.id.tvResultPrice);
        }
    }
}
