package com.riza.ecommerce.data.remote.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProductResponse {
    @SerializedName("products")
    private List<ProductDto> products;

    @SerializedName("total")
    private int total;

    @SerializedName("skip")
    private int skip;

    @SerializedName("limit")
    private int limit;

    public List<ProductDto> getProducts() {
        return products;
    }

    public int getTotal() { return total; }
    public int getSkip() { return skip; }
    public int getLimit() { return limit; }

    public static class ProductDto {
        @SerializedName("id")
        private int id;

        @SerializedName("title")
        private String title;

        @SerializedName("description")
        private String description;

        @SerializedName("price")
        private double price;

        @SerializedName("thumbnail")
        private String thumbnail;

        @SerializedName("stock")
        private int stock;

        @SerializedName("category")
        private String category;

        public int getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public double getPrice() { return price; }
        public String getThumbnail() { return thumbnail; }
        public int getStock() { return stock; }
        public String getCategory() { return category; }
    }
}