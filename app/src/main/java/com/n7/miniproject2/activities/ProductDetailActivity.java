package com.n7.miniproject2.activities;



import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.n7.miniproject2.R;
import com.n7.miniproject2.database.AppDb;
import com.n7.miniproject2.database.daos.OrderDao;
import com.n7.miniproject2.database.daos.ProductDao;
import com.n7.miniproject2.entities.Product;
import com.n7.miniproject2.enums.OrderStatus;
import com.n7.miniproject2.utils.SharedPreferencesUtil;
import com.n7.miniproject2.utils.UserBarHelper;

import java.text.DecimalFormat;
import java.util.concurrent.Executors;

public class ProductDetailActivity extends AppCompatActivity {
    private final int REQUEST_CODE_LOGIN = 1;
    private ProductDao productDao;
    private OrderDao orderDao;
    private Product product;
    private final DecimalFormat formatter = new DecimalFormat("#,###");

    ImageView ivProductImage;
    TextView tvProductName;
    TextView tvProductPrice;
    TextView tvProductUnit;
    TextView tvProductDescription;
    Button btnBuyNow;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initUI();
        setListeners();
        setVariables();
    }

    private void initUI() {
        ivProductImage = findViewById(R.id.ivProductImage);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvProductUnit = findViewById(R.id.tvProductUnit);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        btnBack = findViewById(R.id.btnBack);
        UserBarHelper.setupUserBar(this);
    }

    private void setListeners() {
        btnBack.setOnClickListener(l -> finish());
        btnBuyNow.setOnClickListener(l -> {
            String username = SharedPreferencesUtil.getString(this, "username", null);
            if (username == null) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_CODE_LOGIN);
            } else {
                addProductToOrder();
            }
        });
        UserBarHelper.setupUserBar(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_OK) {
            addProductToOrder();
        }
    }

    private void addProductToOrder() {
        Executors.newSingleThreadExecutor().execute(() -> {
            int userId = Integer.parseInt(SharedPreferencesUtil.getString(this, "userId", "-1"));
            if (userId == -1) {
                runOnUiThread(() -> Toast.makeText(this, "Lỗi đăng nhập", Toast.LENGTH_SHORT).show());
                return;
            }

            orderDao = AppDb.getInstance(this).orderDao();
            var unpaidOrder = orderDao.getUnpaidOrder(userId);

            // Tạo đơn hàng mới nếu chưa có
            if (unpaidOrder == null) {
                var newOrderId = orderDao.createOrder(System.currentTimeMillis(), OrderStatus.CREATED, userId);
                unpaidOrder = orderDao.getOrderById((int)newOrderId);
            }

            // Kiểm tra sản phẩm đã có trong đơn hàng chưa
            boolean exists = orderDao.isProductInOrder(unpaidOrder.getId(), product.getId());
            if (exists) {
                runOnUiThread(() -> Toast.makeText(this, "Sản phẩm này đã có trong giỏ hàng rồi!", Toast.LENGTH_SHORT).show());
                return;
            }

            // Thêm sản phẩm vào đơn hàng
            orderDao.addProductToOrder(unpaidOrder.getId(), product.getId(), 1, product.getPrice());

            runOnUiThread(this::showSelection);
        });
    }

    private void showSelection() {
        new AlertDialog.Builder(this)
                .setTitle("Thành công")
                .setMessage("Sản phẩm đã được thêm vào giỏ hàng. Bạn có muốn thanh toán ngay không?")
                .setPositiveButton("Thanh toán", (dialog, which) -> {
                    startActivity(new Intent(this, EditOrderActivity.class));
                })
                .setNegativeButton("Tiếp tục mua", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private void setVariables() {
        int productId = getIntent().getIntExtra("productId", -1);
        if (productId == -1) {
            finish();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            productDao = AppDb.getInstance(this).productDao();
            product = productDao.getProductById(productId);

            runOnUiThread(() -> {
                if (product == null) {
                    finish();
                    return;
                }

                tvProductName.setText(product.getName());
                tvProductPrice.setText(formatter.format(product.getPrice()) + " VNĐ");
                tvProductUnit.setText(" / " + product.getUnit());
                tvProductDescription.setText(product.getDescription());


            });
        });

        Glide.with(this)
                .load(product.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(ivProductImage);
    }
}