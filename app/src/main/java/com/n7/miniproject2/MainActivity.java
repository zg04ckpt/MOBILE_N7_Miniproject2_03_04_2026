package com.n7.miniproject2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.n7.miniproject2.activities.ListCategoriesActivity;
import com.n7.miniproject2.activities.ListProductsActivity;
import com.n7.miniproject2.activities.LoginActivity;
import com.n7.miniproject2.utils.UserBarHelper;

public class MainActivity extends AppCompatActivity {

    Button btnViewCategories;
    Button btnViewProducts;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserBarHelper.setupUserBar(this);
    }

    private void initUI() {
        btnViewCategories = findViewById(R.id.btnViewCategories);
        btnViewProducts = findViewById(R.id.btnViewProducts);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setListeners() {
        btnViewCategories.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListCategoriesActivity.class);
            startActivity(intent);
        });

        btnViewProducts.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListProductsActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, UserBarHelper.REQUEST_CODE_LOGIN);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UserBarHelper.REQUEST_CODE_LOGIN && resultCode == RESULT_OK) {
            // UserBarHelper.setupUserBar(this) will be called in onResume
        }
    }
}