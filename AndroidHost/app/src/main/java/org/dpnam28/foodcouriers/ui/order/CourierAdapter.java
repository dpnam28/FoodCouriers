package org.dpnam28.foodcouriers.ui.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import org.dpnam28.foodcouriers.R;

import java.util.ArrayList;
import java.util.List;

class CourierAdapter extends BaseAdapter {

    interface CourierSelectListener {
        void onCourierSelected(SelectCourierContract.CourierItem courier);
    }

    private final List<SelectCourierContract.CourierItem> items = new ArrayList<>();
    private final LayoutInflater inflater;
    private CourierSelectListener listener;

    CourierAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    void setCouriers(List<SelectCourierContract.CourierItem> couriers) {
        items.clear();
        if (couriers != null) {
            items.addAll(couriers);
        }
        notifyDataSetChanged();
    }

    void setCourierSelectListener(CourierSelectListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public SelectCourierContract.CourierItem getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_courier_option, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SelectCourierContract.CourierItem courier = getItem(position);
        holder.tvName.setText(courier.getFullName());
        holder.tvPhone.setText(parent.getContext().getString(R.string.courier_phone_format, courier.getPhoneNumber()));
        holder.tvLocation.setText(parent.getContext().getString(R.string.courier_location_format, courier.getLocation()));
        holder.btnSelect.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCourierSelected(courier);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView tvName;
        TextView tvPhone;
        TextView tvLocation;
        AppCompatButton btnSelect;

        ViewHolder(View view) {
            tvName = view.findViewById(R.id.tvCourierName);
            tvPhone = view.findViewById(R.id.tvCourierPhone);
            tvLocation = view.findViewById(R.id.tvCourierLocation);
            btnSelect = view.findViewById(R.id.btnSelectCourier);
        }
    }
}
