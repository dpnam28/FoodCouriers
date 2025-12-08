package org.dpnam28.foodcouriers.ui.login;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import org.dpnam28.foodcouriers.R;

public class LoginActivity extends AppCompatActivity {

    TabLayout tabLayout;
    LinearLayout layoutRegister, layoutLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        tabLayout = findViewById(R.id.tabContainer);
        layoutLogin = findViewById(R.id.layoutLogin);
        layoutRegister = findViewById(R.id.layoutRegister);

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