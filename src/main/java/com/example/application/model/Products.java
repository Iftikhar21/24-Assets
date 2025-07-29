package com.example.application.model;

public class Products {
    private Integer productID;
    private Integer categoryID;
    private String productName;
    private String categoryName;
    private Integer stock;

    private Integer quantity;

    public Products(Integer productID, Integer categoryID, String productName, String categoryName, Integer stock, Integer quantity) {
        this.productID = productID;
        this.categoryID = categoryID;
        this.productName = productName;
        this.categoryName = categoryName;
        this.stock = stock;
        this.quantity = quantity;
    }

    public Products() {}

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
