package org.dpnam28.foodcouriers.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import org.dpnam28.foodcouriers.R;

public class ToastUtils {

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_ERROR   = 1;

    public static void showTopToast(Activity activity, String message, int type) {
        LayoutInflater inflater = activity.getLayoutInflater();

        View layout = inflater.inflate(
                R.layout.layout_custom_toast,
                activity.findViewById(R.id.toast_root)
        );

        TextView tvMessage = layout.findViewById(R.id.tvToastMessage);
        ImageView imgIcon  = layout.findViewById(R.id.imgToastIcon);
        View root          = layout.findViewById(R.id.toast_root);

        tvMessage.setText(message);

        int iconResId;
        @ColorInt int tintColor;
        @ColorInt int strokeColor;

        switch (type) {
            case TYPE_ERROR:
                iconResId   = R.drawable.ic_warn;
                tintColor   = ContextCompat.getColor(activity, android.R.color.holo_red_dark);
                strokeColor = tintColor;
                break;

            case TYPE_SUCCESS:
            default:
                iconResId   = R.drawable.ic_check;
                tintColor   = ContextCompat.getColor(activity, R.color.green); // #D61355
                strokeColor = tintColor;
                break;
        }

        // Đổi icon + tint
        imgIcon.setImageResource(iconResId);
        imgIcon.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);

        // Đổi viền (stroke) của background
        if (root.getBackground() instanceof GradientDrawable) {
            GradientDrawable bg = (GradientDrawable) root.getBackground();
            bg.setStroke(dpToPx(activity, 1), strokeColor);
        }

        Toast toast = new Toast(activity.getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);

        int yOffset = dpToPx(activity, 0);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, yOffset);

        toast.show();
    }

    private static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }
}


