package com.n7.miniproject2.dtos;

import com.n7.miniproject2.enums.OrderStatus;

public class OrderListItemDto {
    private int id;
    private OrderStatus status;
    private Long lastModified;
    private int numberOfProducts;

    public OrderListItemDto(int id, OrderStatus status, Long lastModified, int numberOfProducts) {
        this.id = id;
        this.status = status;
        this.lastModified = lastModified;
        this.numberOfProducts = numberOfProducts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }
}