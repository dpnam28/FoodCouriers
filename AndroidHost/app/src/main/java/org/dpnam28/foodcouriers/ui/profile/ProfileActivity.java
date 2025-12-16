package org.dpnam28.foodcouriers.ui.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.dpnam28.foodcouriers.R;
import org.dpnam28.foodcouriers.ui.login.LoginActivity;

public class ProfileActivity extends AppCompatActivity {

    ImageButton btnBack;
    Button btnLogout, btnEdit;
    TextView tvNameValue, tvAddressValue, tvRoleValue, tvPhoneValue, tvEmailValue, titleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        assignView();
        initialProfile();

        btnBack.setOnClickListener(v -> finish());

        btnEdit.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileEditActivity.class)));

        btnLogout.setOnClickListener(v -> {
            var sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialProfile();
    }

    private void assignView(){
        btnBack = findViewById(R.id.btnBack);
        btnLogout = findViewById(R.id.btnLogout);
        btnEdit = findViewById(R.id.btnEdit);
        tvNameValue = findViewById(R.id.tvNameValue);
        tvAddressValue = findViewById(R.id.tvAddressValue);
        tvRoleValue = findViewById(R.id.tvRoleValue);
        tvPhoneValue = findViewById(R.id.tvPhoneValue);
        tvEmailValue = findViewById(R.id.tvEmailValue);
        titleName = findViewById(R.id.titleName);
    }

    @SuppressLint("SetTextI18n")
    private void initialProfile(){
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        String fullName = sharedPreferences.getString("fullName", "");
        String email = sharedPreferences.getString("email", "");
        String phoneNumber = sharedPreferences.getString("phoneNumber", "");
        String role = sharedPreferences.getString("role", "");
        String address = sharedPreferences.getString("address", "");
        String location = sharedPreferences.getString("location", "");

        String displayRole;
        if ("ROLE_CUSTOMER".equals(role)) {
            displayRole = "Khách hàng";
        } else if ("ROLE_RESTAURANT".equals(role)) {
            displayRole = "Nhà hàng";
            titleName.setText("Tên nhà hàng");
        } else if ("ROLE_COURIER".equals(role)) {
            displayRole = "Nhân viên giao hàng";
        } else {
            displayRole = "";
        }

        tvNameValue.setText(fullName);
        tvAddressValue.setText(String.format("%s, %s", address, location));
        tvRoleValue.setText(displayRole);
        tvPhoneValue.setText(phoneNumber);
        tvEmailValue.setText(email);
    }
}
