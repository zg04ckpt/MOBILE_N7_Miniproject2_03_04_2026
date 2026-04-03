package com.n7.miniproject2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.n7.miniproject2.R;
import com.n7.miniproject2.adapters.ListCategoriesAdapter;
import com.n7.miniproject2.database.AppDb;
import com.n7.miniproject2.database.daos.CategoryDao;
import com.n7.miniproject2.entities.Category;
import com.n7.miniproject2.utils.UserBarHelper;

import java.util.List;
import java.util.concurrent.Executors;

public class ListCategoriesActivity extends AppCompatActivity {

    private CategoryDao categoryDao;

    private RecyclerView rvCategories;
    private ListCategoriesAdapter adapter;
    private List<Category> categories;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_categories);

        initUI();
        setListeners();
        initVariables();
    }

    private void initUI() {
        rvCategories = findViewById(R.id.rvCategories);
        btnBack = findViewById(R.id.btnBack);
        UserBarHelper.setupUserBar(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserBarHelper.setupUserBar(this);
    }

    private void setListeners() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void initVariables() {
        Executors.newSingleThreadExecutor().execute(() -> {
            categoryDao = AppDb.getInstance(this).categoryDao();
            categories = categoryDao.getAllCategories();

            runOnUiThread(() -> {
                rvCategories.setLayoutManager(new GridLayoutManager(this, 2));
                adapter = new ListCategoriesAdapter(categories);
                adapter.setOnItemClicked(category -> {
                    Intent intent = new Intent(this, ListProductsActivity.class);
                    intent.putExtra("categoryId", category.getId());
                    startActivity(intent);
                });
                rvCategories.setAdapter(adapter);
            });
        });
    }
}