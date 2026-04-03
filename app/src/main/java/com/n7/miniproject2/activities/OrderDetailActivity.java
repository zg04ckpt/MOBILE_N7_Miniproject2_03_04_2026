package com.n7.miniproject2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.n7.miniproject2.MainActivity;
import com.n7.miniproject2.R;
import com.n7.miniproject2.adapters.ListOrderDetailsAdapter;
import com.n7.miniproject2.database.AppDb;
import com.n7.miniproject2.database.daos.OrderDao;
import com.n7.miniproject2.dtos.OrderDetailDto;
import com.n7.miniproject2.entities.Order;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class OrderDetailActivity extends AppCompatActivity {
    private OrderDao orderDao;
    private Order order;
    private List<OrderDetailDto> orderDetails;
    private ListOrderDetailsAdapter orderDetailsAdapter;
    private final DecimalFormat formatter = new DecimalFormat("#,###");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    TextView tvOrderId;
    TextView tvOrderDate;
    TextView tvPaymentDate;
    TextView tvOrderStatus;
    TextView tvTotalAmount;
    RecyclerView rvOrderItemsReadOnly;
    Button btnBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        initUI();
        setListeners();
        setVariables();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initUI() {
        tvOrderId = findViewById(R.id.tvOrderId);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvPaymentDate = findViewById(R.id.tvPaymentDate);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        rvOrderItemsReadOnly = findViewById(R.id.rvOrderItemsReadOnly);
        btnBackToHome = findViewById(R.id.btnBackToHome);
    }

    private void setListeners() {
        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void setVariables() {
        int orderId = getIntent().getIntExtra("orderId", -1);
        if (orderId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            orderDao = AppDb.getInstance(this).orderDao();
            order = orderDao.getOrderById(orderId);
            orderDetails = orderDao.getOrderDetailsByOrderId(orderId);

            if (order == null) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Đơn hàng không tồn tại", Toast.LENGTH_SHORT).show();
                    finish();
                });
                return;
            }

            runOnUiThread(() -> {
                rvOrderItemsReadOnly.setLayoutManager(new LinearLayoutManager(this));
                orderDetailsAdapter = new ListOrderDetailsAdapter(orderDetails);
                rvOrderItemsReadOnly.setAdapter(orderDetailsAdapter);
                updateInfo();
            });
        });
    }

    private void updateInfo() {
        if (order == null || orderDetails == null) return;

        tvOrderId.setText("#" + order.getId());
        tvOrderDate.setText(dateFormat.format(new Date(order.getCreatedAt())));

        if (order.getLastModified() != null) {
            tvPaymentDate.setText(dateFormat.format(new Date(order.getLastModified())));
        } else {
            tvPaymentDate.setText("Chưa thanh toán");
        }

        tvOrderStatus.setText(order.getStatus().toString());

        double total = 0;
        for (OrderDetailDto detail : orderDetails) {
            total += detail.getPrice() * detail.getQuantity();
        }
        tvTotalAmount.setText(formatter.format(total) + " VNĐ");
    }
}