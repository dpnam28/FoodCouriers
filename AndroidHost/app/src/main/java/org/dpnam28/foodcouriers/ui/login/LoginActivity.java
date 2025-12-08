package org.dpnam28.foodcouriers.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import org.dpnam28.foodcouriers.R;
import org.dpnam28.foodcouriers.ui.main.MainActivity;
import org.dpnam28.foodcouriers.utils.ToastUtils;

public class LoginActivity extends AppCompatActivity {

    TabLayout tabLayout;
    LinearLayout layoutRegister, layoutLogin;
    EditText edtEmailLogin, edtPasswordLogin, edtFullNameRegister, edtEmailRegister, edtPasswordRegister;
    Button btnLogin, btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        assignView();

        TabLayout.Tab tab = tabLayout.getTabAt(0);
        if (tab != null) {
            tab.select();
        }
        showTab(0);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btnLogin.setOnClickListener(v -> {
            String email = edtEmailLogin.getText().toString();
            String password = edtPasswordLogin.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                ToastUtils.showTopToast(this, "Please fill all fields", ToastUtils.TYPE_ERROR);
            } else {
                if (email.equals("admin@gmail.com") && password.equals("admin")) {
                    ToastUtils.showTopToast(this, "Login successful", ToastUtils.TYPE_SUCCESS);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtils.showTopToast(this, "Login failed", ToastUtils.TYPE_ERROR);
                }
            }
        });
    }

    private void assignView() {
        tabLayout = findViewById(R.id.tabContainer);
        layoutLogin = findViewById(R.id.layoutLogin);
        layoutRegister = findViewById(R.id.layoutRegister);
        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
        edtFullNameRegister = findViewById(R.id.edtFullNameRegister);
        edtEmailRegister = findViewById(R.id.edtEmailRegister);
        edtPasswordRegister = findViewById(R.id.edtPasswordRegister);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnSignUp);
    }

    private void showTab(int position) {
        if (position == 0) {
            layoutRegister.setVisibility(LinearLayout.VISIBLE);
            layoutLogin.setVisibility(LinearLayout.GONE);
        } else if (position == 1) {
            layoutRegister.setVisibility(LinearLayout.GONE);
            layoutLogin.setVisibility(LinearLayout.VISIBLE);
        }
    }
}