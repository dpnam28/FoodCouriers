package org.dpnam28.foodcouriers.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import org.dpnam28.foodcouriers.R;
import org.dpnam28.foodcouriers.ui.cart.CartActivity;
import org.dpnam28.foodcouriers.ui.fooddetail.FoodDetailActivity;
import org.dpnam28.foodcouriers.ui.menu.RestaurantMenuActivity;
import org.dpnam28.foodcouriers.ui.profile.ProfileActivity;

public class MainActivity extends AppCompatActivity {

    private ImageButton imgBurger1;
    private LinearLayout navHome, navCart, navProfile, navMenu, navHistory;
    private NestedScrollView scrollContentMain;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        assignView();
        userRole = getSharedPreferences("userInfo", MODE_PRIVATE).getString("role", "");
        setupBottomNavForRole();

        imgBurger1.setOnClickListener(v -> startActivity(new Intent(this, FoodDetailActivity.class)));
        navHome.setOnClickListener(v -> scrollContentMain.smoothScrollTo(0, 0));
        navCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        navProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        navMenu.setOnClickListener(v -> {
            if ("ROLE_RESTAURANT".equals(userRole)) {
                startActivity(new Intent(this, RestaurantMenuActivity.class));
            }
        });
    }

    private void assignView() {
        imgBurger1 = findViewById(R.id.imgBurger1);
        navHome = findViewById(R.id.navHome);
        navCart = findViewById(R.id.navCart);
        navProfile = findViewById(R.id.navProfile);
        navMenu = findViewById(R.id.navMenu);
        navHistory = findViewById(R.id.navHistory);
        scrollContentMain = findViewById(R.id.scrollContentMain);
    }

    private void setupBottomNavForRole() {
        if ("ROLE_RESTAURANT".equals(userRole)) {
            navCart.setVisibility(View.GONE);
            navHistory.setVisibility(View.GONE);
            navMenu.setVisibility(View.VISIBLE);
        } else {
            navCart.setVisibility(View.VISIBLE);
            navHistory.setVisibility(View.VISIBLE);
            navMenu.setVisibility(View.GONE);
        }
    }
}
