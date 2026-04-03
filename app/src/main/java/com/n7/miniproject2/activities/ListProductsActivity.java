package com.n7.miniproject2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.n7.miniproject2.R;
import com.n7.miniproject2.adapters.ListProductsAdapter;
import com.n7.miniproject2.database.AppDb;
import com.n7.miniproject2.database.daos.CategoryDao;
import com.n7.miniproject2.database.daos.ProductDao;
import com.n7.miniproject2.dtos.ProductListItemDto;
import com.n7.miniproject2.entities.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ListProductsActivity extends AppCompatActivity {
    private ProductDao productDao;
    private CategoryDao categoryDao;

    EditText etSearchProductName;
    Spinner spnCategories;
    RecyclerView rvProducts;
    Button btnBackToMain;

    private ListProductsAdapter productsAdapter;
    private List<Category> categoriesList = new ArrayList<>();
    private List<ProductListItemDto> allProductsList = new ArrayList<>();
    private int selectedCategoryIdFromIntent = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);

        selectedCategoryIdFromIntent = getIntent().getIntExtra("categoryId", -1);

        initUI();
        setListeners();
        initData();
    }

    private void initUI() {
        etSearchProductName = findViewById(R.id.etSearchProductName);
        spnCategories = findViewById(R.id.spnCategories);
        rvProducts = findViewById(R.id.rvProducts);
        btnBackToMain = findViewById(R.id.btnBackToMain);

        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        productsAdapter = new ListProductsAdapter(new ArrayList<>());
        rvProducts.setAdapter(productsAdapter);
    }

    private void setListeners() {
        btnBackToMain.setOnClickListener(l -> finish());

        spnCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilter();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        etSearchProductName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilter();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        productsAdapter.setOnItemClicked(product -> {
            if (product != null) {
                Intent intent = new Intent(this, ProductDetailActivity.class);
                intent.putExtra("productId", product.getId());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDb db = AppDb.getInstance(this);
            categoryDao = db.categoryDao();
            productDao = db.productDao();

            categoriesList = categoryDao.getAllCategories();
            allProductsList = productDao.getAllProducts();

            List<String> categoryNames = new ArrayList<>();
            categoryNames.add("Tất cả danh mục");
            for (Category c : categoriesList) {
                categoryNames.add(c.getName());
            }

            runOnUiThread(() -> {
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnCategories.setAdapter(spinnerAdapter);

                if (selectedCategoryIdFromIntent != -1) {
                    for (int i = 0; i < categoriesList.size(); i++) {
                        if (categoriesList.get(i).getId() == selectedCategoryIdFromIntent) {
                            // Cập nhật spinner selection (i+1 vì vị trí 0 là "Tất cả danh mục")
                            spnCategories.setSelection(i + 1);
                            break;
                        }
                    }
                }
                applyFilter();
            });
        });
    }

    private void applyFilter() {
        if (allProductsList == null || allProductsList.isEmpty()) return;

        String searchText = etSearchProductName.getText().toString().trim().toLowerCase();
        Object selectedItem = spnCategories.getSelectedItem();
        String selectedCategoryName = (selectedItem != null) ? selectedItem.toString() : "Tất cả danh mục";

        List<ProductListItemDto> filtered;

        // Lọc theo danh mục
        if (selectedCategoryName.equals("Tất cả danh mục")) {
            filtered = new ArrayList<>(allProductsList);
        } else {
            final String finalCategoryName = selectedCategoryName;
            filtered = allProductsList.stream()
                    .filter(p -> p.getCategoryName() != null && p.getCategoryName().equalsIgnoreCase(finalCategoryName))
                    .collect(Collectors.toList());
        }

        // Lọc theo tên
        if (!searchText.isEmpty()) {
            final String finalSearchText = searchText;
            filtered = filtered.stream()
                    .filter(p -> p.getName() != null && p.getName().toLowerCase().contains(finalSearchText))
                    .collect(Collectors.toList());
        }

        productsAdapter.setProducts(filtered);
    }
}
