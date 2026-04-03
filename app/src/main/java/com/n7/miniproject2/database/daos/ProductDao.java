package com.n7.miniproject2.database.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.n7.miniproject2.dtos.ProductListItemDto;
import com.n7.miniproject2.entities.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    // Lấy tất cả
    @Query("SELECT p.*, c.name AS categoryName " +
            "FROM Products p " +
            "JOIN Categories c ON p.categoryId = c.id")
    List<ProductListItemDto> getAllProducts();

    // Lọc theo danh mục
    @Query("SELECT p.*, c.name AS categoryName " +
            "FROM Products p " +
            "JOIN Categories c ON p.categoryId = c.id " +
            " WHERE p.categoryId = :categoryId")
    List<ProductListItemDto> getProductsByCategory(int categoryId);

    // Lấy tất cả thông tin 1 sản phẩm
    @Query("SELECT * FROM Products WHERE id = :productId")
    Product getProductById(int productId);
}