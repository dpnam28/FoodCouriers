package org.dpnam28.foodcouriers.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import org.dpnam28.foodcouriers.R;
import org.dpnam28.foodcouriers.ui.cart.CartActivity;
import org.dpnam28.foodcouriers.ui.fooddetail.FoodDetailActivity;
import org.dpnam28.foodcouriers.ui.profile.ProfileActivity;

public class MainActivity extends AppCompatActivity {

    ImageButton imgBurger1;
    LinearLayout navHome, navCart, navProfile;
    NestedScrollView scrollContentMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        assignView();

        imgBurger1.setOnClickListener(v -> {
            Intent intent = new Intent(this, FoodDetailActivity.class);
            startActivity(intent);
        });
        navHome.setOnClickListener(v -> scrollContentMain.smoothScrollTo(0, 0));
        navCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void assignView(){
        imgBurger1 = findViewById(R.id.imgBurger1);
        navHome = findViewById(R.id.navHome);
        scrollContentMain = findViewById(R.id.scrollContentMain);
        navCart = findViewById(R.id.navCart);
        navProfile = findViewById(R.id.navProfile);
    }
}