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

public class MainActivity extends AppCompatActivity {

    ImageButton imgBurger1, btnCart;
    LinearLayout navHome;
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
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void assignView(){
        imgBurger1 = findViewById(R.id.imgBurger1);
        navHome = findViewById(R.id.navHome);
        scrollContentMain = findViewById(R.id.scrollContentMain);
        btnCart = findViewById(R.id.btnCart);
    }
}