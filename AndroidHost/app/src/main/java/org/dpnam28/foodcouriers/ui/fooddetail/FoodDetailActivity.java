package org.dpnam28.foodcouriers.ui.fooddetail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.dpnam28.foodcouriers.R;
import org.dpnam28.foodcouriers.ui.cart.CartActivity;

public class FoodDetailActivity extends AppCompatActivity {

    ImageButton btnBack;
    Button btnAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_detail);
        assignView();

        btnBack.setOnClickListener(v -> finish());

        btnAddToCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void assignView(){
        btnBack = findViewById(R.id.btnBack);
        btnAddToCart = findViewById(R.id.btnAddToCart);
    }
}