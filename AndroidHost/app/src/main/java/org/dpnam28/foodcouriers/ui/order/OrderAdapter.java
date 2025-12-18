package org.dpnam28.foodcouriers.ui.order;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import org.dpnam28.foodcouriers.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class OrderAdapter extends BaseAdapter {

    interface OrderActionListener {
        void onCancelOrder(OrderModel order);

        void onAcceptOrder(OrderModel order);

        void onMarkDelivered(OrderModel order);
    }

    private final List<OrderModel> items = new ArrayList<>();
    private final LayoutInflater inflater;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private final Context context;
    private final String userRole;
    private OrderActionListener actionListener;

    OrderAdapter(Context context, String userRole) {
        this.context = context;
        this.userRole = userRole;
        this.inflater = LayoutInflater.from(context);
    }

    void setItems(List<OrderModel> data) {
        items.clear();
        if (data != null) {
            items.addAll(data);
        }
        notifyDataSetChanged();
    }

    void setActionListener(OrderActionListener listener) {
        this.actionListener = listener;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public OrderModel getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_order, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderModel order = getItem(position);
        bindTitle(holder, order);
        holder.tvFoods.setText(context.getString(R.string.order_foods_label, buildFoodSummary(order)));
        holder.tvStatus.setText(order.getStatusLabel());
        holder.tvTotal.setText(context.getString(R.string.order_total_format, currencyFormat.format(order.getTotalPrice())));
        bindActions(holder, order);

        return convertView;
    }

    private void bindTitle(ViewHolder holder, OrderModel order) {
        OrderModel.RestaurantInfo restaurant = order.getRestaurant();
        OrderModel.CustomerInfo customer = order.getCustomer();
        boolean isRestaurant = "ROLE_RESTAURANT".equals(userRole);
        boolean isCustomer = "ROLE_CUSTOMER".equals(userRole);

        if (isRestaurant && customer != null) {
            holder.tvTitle.setText(customer.getFullName());
            holder.tvSubtitle.setText(context.getString(
                    R.string.order_customer_contact_format,
                    safeText(customer.getAddress()),
                    safeText(customer.getPhoneNumber())
            ));
        } else if (isCustomer && restaurant != null) {
            holder.tvTitle.setText(restaurant.getName());
            holder.tvSubtitle.setText(context.getString(
                    R.string.order_customer_contact_format,
                    safeText(restaurant.getAddress()),
                    safeText(restaurant.getPhoneNumber())
            ));
        } else if ("ROLE_COURIER".equals(userRole) && restaurant != null && customer != null) {
            holder.tvTitle.setText(context.getString(
                    R.string.order_courier_route_format,
                    restaurant.getName(),
                    customer.getFullName()
            ));
            holder.tvSubtitle.setText(context.getString(
                    R.string.order_customer_contact_format,
                    safeText(customer.getAddress()),
                    safeText(customer.getPhoneNumber())
            ));
        } else {
            holder.tvTitle.setText(restaurant != null ? restaurant.getName() : context.getString(R.string.orders_title));
            holder.tvSubtitle.setText("");
        }
    }

    private void bindActions(ViewHolder holder, OrderModel order) {
        boolean isRestaurant = "ROLE_RESTAURANT".equals(userRole);
        boolean isCustomer = "ROLE_CUSTOMER".equals(userRole);
        boolean isCourier = "ROLE_COURIER".equals(userRole);

        boolean canCancel = order.canCancel() && (isRestaurant || isCustomer);
        boolean canAccept = order.canAccept() && isRestaurant;
        boolean canDeliver = isCourier && "ACCEPTED".equals(order.getStatus());

        holder.btnCancel.setVisibility(canCancel ? View.VISIBLE : View.GONE);
        holder.btnAccept.setVisibility((canAccept || canDeliver) ? View.VISIBLE : View.GONE);
        holder.btnAccept.setText(canDeliver ? context.getString(R.string.order_mark_delivered) : context.getString(R.string.order_accept));
        holder.layoutActions.setVisibility((canCancel || canAccept || canDeliver) ? View.VISIBLE : View.GONE);

        holder.btnCancel.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onCancelOrder(order);
            }
        });
        holder.btnAccept.setOnClickListener(v -> {
            if (actionListener == null) return;
            if (canDeliver) {
                actionListener.onMarkDelivered(order);
            } else {
                actionListener.onAcceptOrder(order);
            }
        });
    }

    private String buildFoodSummary(OrderModel order) {
        List<OrderModel.OrderItem> orderItems = order.getItems();
        if (orderItems == null || orderItems.isEmpty()) {
            return context.getString(R.string.order_foods_empty);
        }
        List<String> parts = new ArrayList<>();
        for (OrderModel.OrderItem item : orderItems) {
            parts.add(item.getQuantity() + " x " + item.getFoodName());
        }
        return TextUtils.join(", ", parts);
    }

    private String safeText(String text) {
        return text == null ? "" : text;
    }

    static class ViewHolder {
        TextView tvTitle;
        TextView tvSubtitle;
        TextView tvFoods;
        TextView tvStatus;
        TextView tvTotal;
        LinearLayout layoutActions;
        AppCompatButton btnCancel;
        AppCompatButton btnAccept;

        ViewHolder(View view) {
            tvTitle = view.findViewById(R.id.tvOrderTitle);
            tvSubtitle = view.findViewById(R.id.tvOrderSubtitle);
            tvFoods = view.findViewById(R.id.tvOrderFoods);
            tvStatus = view.findViewById(R.id.tvOrderStatus);
            tvTotal = view.findViewById(R.id.tvOrderTotal);
            layoutActions = view.findViewById(R.id.layoutOrderActions);
            btnCancel = view.findViewById(R.id.btnCancelOrder);
            btnAccept = view.findViewById(R.id.btnAcceptOrder);
        }
    }
}
