package org.dpnam28.foodcouriers.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import org.dpnam28.foodcouriers.R;
import org.dpnam28.foodcouriers.ui.main.MainActivity;
import org.dpnam28.foodcouriers.utils.ToastUtils;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private static final String TAG = "LoginActivity";

    private TabLayout tabLayout;
    private View layoutRegister;
    private View layoutLogin;
    private EditText edtEmailLogin;
    private EditText edtPasswordLogin;
    private EditText edtFullNameRegister;
    private EditText edtEmailRegister;
    private EditText edtPasswordRegister;
    private EditText edtPhoneRegister;
    private EditText edtAddressRegister;
    private Spinner spinnerCityRegister;
    private Spinner spinnerRoleRegister;
    private Button btnLogin;
    private Button btnRegister;
    private ProgressBar progressLoading;

    private LoginContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        assignView();
        presenter = new LoginPresenter(this, this);

        TabLayout.Tab tab = tabLayout.getTabAt(1);
        if (tab != null) {
            tab.select();
        }
        showTab(1);

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

        btnRegister.setOnClickListener(v -> handleRegister());

        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleRegister() {
        String fullName = getTrimmed(edtFullNameRegister);
        String email = getTrimmed(edtEmailRegister);
        String password = getTrimmed(edtPasswordRegister);
        String phoneNumber = getTrimmed(edtPhoneRegister);
        String address = getTrimmed(edtAddressRegister);
        String role = getSelectedRole();
        String city = getSelectedCity();

        if (TextUtils.isEmpty(fullName) ||
                TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(phoneNumber) ||
                TextUtils.isEmpty(address) ||
                TextUtils.isEmpty(city)) {
            ToastUtils.showTopToast(this, "Vui lòng nhập đầy đủ thông tin", ToastUtils.TYPE_ERROR);
            return;
        }

        if (TextUtils.isEmpty(role)) {
            ToastUtils.showTopToast(this, "Vui lòng chọn vai trò", ToastUtils.TYPE_ERROR);
            return;
        }

        if(TextUtils.isEmpty(city)) {
            ToastUtils.showTopToast(this, "Vui lòng chọn thành phố", ToastUtils.TYPE_ERROR);
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ToastUtils.showTopToast(this, "Email không hợp lệ", ToastUtils.TYPE_ERROR);
            return;
        }

        LoginContract.UserForm form = new LoginContract.UserForm(
                fullName,
                email,
                password,
                phoneNumber,
                address,
                role,
                Long.parseLong(city)
        );
        btnRegister.setEnabled(false);
        presenter.register(form);
        btnRegister.setEnabled(true);
    }

    private void handleLogin(){
        String email = getTrimmed(edtEmailLogin);
        String password = getTrimmed(edtPasswordLogin);

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            ToastUtils.showTopToast(this, "Vui lòng nhập đầy đủ thông tin", ToastUtils.TYPE_ERROR);
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ToastUtils.showTopToast(this, "Email không hợp lệ", ToastUtils.TYPE_ERROR);
            return;
        }

        LoginContract.UserLoginForm form = new LoginContract.UserLoginForm(email, password);
        btnLogin.setEnabled(false);
        presenter.login(form);
        btnLogin.setEnabled(true);
    }

    private String getTrimmed(EditText editText) {
        return editText.getText().toString().trim();
    }

    private String getSelectedRole() {
        if (spinnerRoleRegister == null) {
            return "";
        }
        int position = spinnerRoleRegister.getSelectedItemPosition();
        String[] roleValues = getResources().getStringArray(R.array.register_roles_values);
        if (position < 0 || position >= roleValues.length) {
            return "";
        }
        return roleValues[position];
    }

    private String getSelectedCity(){
        if(spinnerCityRegister == null){
            return "";
        }
        int position = spinnerCityRegister.getSelectedItemPosition();
        String[] cityValues = getResources().getStringArray(R.array.register_city_values);
        if(position < 0 || position >= cityValues.length){
            return "";
        }
        return cityValues[position];
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
        edtPhoneRegister = findViewById(R.id.edtPhoneRegister);
        edtAddressRegister = findViewById(R.id.edtAddressRegister);
        spinnerCityRegister = findViewById(R.id.spinnerCityRegister);
        spinnerRoleRegister = findViewById(R.id.spinnerRoleRegister);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnSignUp);
        progressLoading = findViewById(R.id.progressLoading);
    }

    private void showTab(int position) {
        if (position == 0) {
            layoutRegister.setVisibility(View.VISIBLE);
            layoutLogin.setVisibility(View.GONE);
        } else if (position == 1) {
            layoutRegister.setVisibility(View.GONE);
            layoutLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showLoading(boolean isLoading) {
        progressLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!isLoading);
        if (isLoading) {
            btnRegister.setAlpha(0.7f);
        } else {
            btnRegister.setAlpha(1f);
        }
    }

    @Override
    public void onRegisterSuccess() {
        ToastUtils.showTopToast(this, "Đăng ký thành công", ToastUtils.TYPE_SUCCESS);
        clearRegisterForm();
        TabLayout.Tab loginTab = tabLayout.getTabAt(1);
        if (loginTab != null) {
            loginTab.select();
        }
        showTab(1);
    }

    @Override
    public void onRegisterError(String message) {
        String displayMessage = TextUtils.isEmpty(message) ? "Đăng ký thất bại" : message;
        Log.e(TAG, "Registration error: " + displayMessage);
        ToastUtils.showTopToast(this, displayMessage, ToastUtils.TYPE_ERROR);
    }

    @Override
    public void onLoginSuccess() {
        ToastUtils.showTopToast(this, "Đăng nhập thành công", ToastUtils.TYPE_SUCCESS);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginError(String message) {
        String displayMessage = TextUtils.isEmpty(message) ? "Đăng nhập thất bại" : message;
        Log.e(TAG, "Login error: " + displayMessage);
        ToastUtils.showTopToast(this, displayMessage, ToastUtils.TYPE_ERROR);
    }

    private void clearRegisterForm() {
        edtFullNameRegister.setText("");
        edtEmailRegister.setText("");
        edtPasswordRegister.setText("");
        edtPhoneRegister.setText("");
        edtAddressRegister.setText("");
        if (spinnerRoleRegister != null) {
            spinnerRoleRegister.setSelection(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detach();
        }
    }
}
