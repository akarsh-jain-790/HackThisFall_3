package com.example.shoparoundsup.model;

import android.net.Uri;

public class InventoryItemModel {

    String Name, Price, ItemImage;

    public InventoryItemModel(String name, String price, String itemImage) {
        Name = name;
        Price = price;
        ItemImage = itemImage;
    }

    public InventoryItemModel() {
    }

    public String getItemImage() {
        return ItemImage;
    }

    public void setItemImage(String itemImage) {
        ItemImage = itemImage;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
