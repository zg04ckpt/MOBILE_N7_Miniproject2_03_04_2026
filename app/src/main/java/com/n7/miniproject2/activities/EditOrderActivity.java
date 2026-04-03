package com.n7.miniproject2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.n7.miniproject2.R;
import com.n7.miniproject2.adapters.ListEditProductOrderAdapter;
import com.n7.miniproject2.database.AppDb;
import com.n7.miniproject2.database.daos.OrderDao;
import com.n7.miniproject2.dtos.OrderDetailDto;
import com.n7.miniproject2.entities.Order;
import com.n7.miniproject2.utils.SharedPreferencesUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class EditOrderActivity extends AppCompatActivity {
    private OrderDao orderDao;
    private Order order;
    private ListEditProductOrderAdapter orderDetailAdapter;
    private List<OrderDetailDto> orderDetails;
    private final DecimalFormat formatter = new DecimalFormat("#,###");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    TextView tvOrderTitle;
    TextView tvOrderId;
    TextView tvOrderDate;
    TextView tvOrderStatus;
    TextView tvTotalAmount;
    RecyclerView rvOrderDetails;
    Button btnPayment;
    Button btnContinueShopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

        initUI();
        setListeners();
        setVariables();
    }

    private void initUI() {
        tvOrderTitle = findViewById(R.id.tvOrderTitle);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        rvOrderDetails = findViewById(R.id.rvOrderDetails);
        btnPayment = findViewById(R.id.btnPayment);
        btnContinueShopping = findViewById(R.id.btnContinueShopping);
    }

    private void setListeners() {
        btnPayment.setOnClickListener(l -> {
            if (order == null || orderDetails == null) return;

            Executors.newSingleThreadExecutor().execute(() -> {
                orderDao.checkout(order.getId(), orderDetails);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, OrderDetailActivity.class);
                    intent.putExtra("orderId", order.getId());
                    startActivity(intent);
                    finish();
                });
            });
        });

        btnContinueShopping.setOnClickListener(l -> {
            finish();
        });
    }

    private void setVariables() {
        String userIdStr = SharedPreferencesUtil.getString(this, "userId", "-1");
        int userId = Integer.parseInt(userIdStr);
        if (userId == -1) {
            Toast.makeText(this, "Lỗi đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Executors.newSingleThreadExecutor().execute(() -> {
            orderDao = AppDb.getInstance(this).orderDao();
            order = orderDao.getUnpaidOrder(userId);
            if (order == null) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Không có đơn hàng nào", Toast.LENGTH_SHORT).show();
                    finish();
                });
                return;
            }

            orderDetails = orderDao.getOrderDetailsByOrderId(order.getId());

            runOnUiThread(() -> {
                rvOrderDetails.setLayoutManager(new LinearLayoutManager(this));
                orderDetailAdapter = new ListEditProductOrderAdapter(orderDetails);
                rvOrderDetails.setAdapter(orderDetailAdapter);
                updateInfo();
            });
        });
    }

    private void updateInfo() {
        if (order == null || orderDetails == null) return;

        tvOrderId.setText("#" + order.getId());
        tvOrderDate.setText(dateFormat.format(new Date(order.getCreatedAt())));
        tvOrderStatus.setText(order.getStatus().toString());

        double total = 0;
        for (OrderDetailDto detail : orderDetails) {
            total += detail.getPrice() * detail.getQuantity();
        }
        tvTotalAmount.setText(formatter.format(total) + " VNĐ");

        if (orderDetailAdapter != null) {
            orderDetailAdapter.notifyDataSetChanged();
        }
    }
}
