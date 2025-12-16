package org.dpnam28.foodcouriers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.dpnam28.foodcouriers.ui.login.LoginActivity;
import org.dpnam28.foodcouriers.ui.main.MainActivity;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharePreference = getSharedPreferences("userPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharePreference.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            }, 1500);
        }else{
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                finish();
            }, 1500);
        }
    }
}