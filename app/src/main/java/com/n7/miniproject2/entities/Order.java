package com.n7.miniproject2.entities;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.n7.miniproject2.enums.OrderStatus;


@Entity(
        tableName = "Orders",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.RESTRICT
        ),
        indices = @Index("userId")
)
public class Order {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private long createdAt;
    private OrderStatus status;
    @Nullable
    private Long lastModified;
    private int userId;

    public Order(int id, long createdAt, OrderStatus status, @Nullable Long lastModified, int userId) {
        this.id = id;
        this.createdAt = createdAt;
        this.status = status;
        this.lastModified = lastModified;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Nullable
    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(@Nullable Long lastModified) {
        this.lastModified = lastModified;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}