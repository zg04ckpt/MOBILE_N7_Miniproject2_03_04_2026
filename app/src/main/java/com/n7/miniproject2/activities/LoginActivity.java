package com.n7.miniproject2.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.n7.miniproject2.R;
import com.n7.miniproject2.database.AppDb;
import com.n7.miniproject2.database.daos.UserDao;
import com.n7.miniproject2.utils.SharedPreferencesUtil;

import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private UserDao userDao;

    private Button btnBack;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
        setListeners();
        initVariables();
    }

    private void initUI() {
        btnBack = findViewById(R.id.btnBack);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnLogin.setOnClickListener(v -> login());
    }

    private void initVariables() {
        userDao = AppDb.getInstance(this).userDao();
    }

    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
            etUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            var user = userDao.getUser(username, password);
            if (user != null) {
                SharedPreferencesUtil.saveString(this, "username", username);
                SharedPreferencesUtil.saveString(this, "avatarUrl", user.getAvatarUrl());
                SharedPreferencesUtil.saveString(this, "userId", String.valueOf(user.getId()));

                runOnUiThread(() -> {
                    Toast.makeText(this, "Đăng nhập thành công! Xin chào " + username, Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show());
            }
        });
    }
}