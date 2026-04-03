package com.n7.miniproject2.dtos;

public class OrderDetailDto {
    private int productId;
    private String productName;
    private String productUrl;
    private double price;
    private int quantity;

    public OrderDetailDto(int productId, String productName, String productUrl, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productUrl = productUrl;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}