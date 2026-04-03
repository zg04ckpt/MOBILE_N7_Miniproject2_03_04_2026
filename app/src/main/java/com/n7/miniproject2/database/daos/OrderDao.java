package com.n7.miniproject2.database.daos;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.n7.miniproject2.dtos.OrderDetailDto;
import com.n7.miniproject2.dtos.OrderListItemDto;
import com.n7.miniproject2.entities.Order;
import com.n7.miniproject2.enums.OrderStatus;

import java.util.List;

@Dao
public interface OrderDao {
    // Xem thông tin tất cả hóa đơn người dùng
    @Query("SELECT *, (SELECT COUNT(*) FROM OrderDetails WHERE orderId = Orders.id) AS numberOfProducts " +
            " FROM Orders WHERE userId = :userId")
    List<OrderListItemDto> getOrdersByUserId(int userId);

    @Query("SELECT * FROM Orders WHERE userId = :userId AND status = 'CREATED' LIMIT 1")
    Order getUnpaidOrder(int userId);

    // Xem thông tin cơ bản của hóa đơn
    @Query("SELECT * FROM Orders WHERE id = :orderId")
    Order getOrderById(int orderId);

    // Xem thông tin chi tiết của hóa đơn
    @Query("SELECT *, Products.name AS productName, Products.imageUrl AS productUrl " +
            "FROM OrderDetails " +
            "JOIN Products ON OrderDetails.productId = Products.id " +
            "WHERE OrderDetails.orderId = :orderId")
    List<OrderDetailDto> getOrderDetailsByOrderId(int orderId);

    // Cập nhật trạng thái 1 hóa đơn
    @Query("UPDATE Orders SET status = :status, lastModified = :lastModified WHERE id = :orderId")
    void updateOrderStatus(int orderId, OrderStatus status, long lastModified);

    // Tạo 1 hóa đơn mới
    @Query("INSERT INTO Orders (createdAt, status, userId) VALUES (:createdAt, :status, :userId)")
    long createOrder(long createdAt, OrderStatus status, int userId);

    // Thêm sản phầm vào hóa đơn
    @Query("INSERT INTO OrderDetails (orderId, productId, quantity, price) VALUES (:orderId, :productId, :quantity, :price)")
    void addProductToOrder(int orderId, int productId, int quantity, double price);

    @Query("UPDATE OrderDetails SET quantity = :quantity WHERE orderId = :orderId AND productId = :productId")
    void updateOrderDetailQuantity(int orderId, int productId, int quantity);

    @Query("DELETE FROM OrderDetails WHERE orderId = :orderId AND productId = :productId")
    void deleteOrderDetail(int orderId, int productId);

    @Query("SELECT EXISTS(SELECT 1 FROM OrderDetails WHERE orderId = :orderId AND productId = :productId)")
    boolean isProductInOrder(int orderId, int productId);

    @Transaction
    default void checkout(int orderId, List<OrderDetailDto> details) {
        for (OrderDetailDto detail : details) {
            if (detail.getQuantity() > 0) {
                updateOrderDetailQuantity(orderId, detail.getProductId(), detail.getQuantity());
            } else {
                deleteOrderDetail(orderId, detail.getProductId());
            }
        }
        updateOrderStatus(orderId, OrderStatus.PAID, System.currentTimeMillis());
    }
}