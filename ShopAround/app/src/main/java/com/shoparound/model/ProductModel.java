package com.shoparound.model;

public class ProductModel {
    String name, price, itemImage;

    public ProductModel(String name, String price, String image) {
        this.name = name;
        this.price = price;
        this.itemImage = image;
    }

    public ProductModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }
}
