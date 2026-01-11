package com.riza.ecommerce.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_items")
public class CartItem {
    @PrimaryKey
    private int productId;
    private String title;
    private double price;
    private String thumbnail;
    private int quantity;
    private int stock;

    public CartItem(int productId, String title, double price, String thumbnail,
                    int quantity, int stock) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.thumbnail = thumbnail;
        this.quantity = quantity;
        this.stock = stock;
    }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public double getTotalPrice() {
        return price * quantity;
    }
}