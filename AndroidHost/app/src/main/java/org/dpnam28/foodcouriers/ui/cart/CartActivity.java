package org.dpnam28.foodcouriers.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.dpnam28.foodcouriers.FinishScreenActivity;
import org.dpnam28.foodcouriers.R;

public class CartActivity extends AppCompatActivity {

    ImageButton btnBack;
    Button btnPlaceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        assignView();

        btnBack.setOnClickListener(v -> finish());

        btnPlaceOrder.setOnClickListener(v -> {
            Intent intent = new Intent(this, FinishScreenActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void assignView(){
        btnBack = findViewById(R.id.btnBack);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
    }
}