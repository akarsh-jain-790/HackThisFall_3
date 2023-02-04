package com.example.shoparoundsup.model;

import android.net.Uri;

public class InventoryItemModel {

    String Name, Price;
    Uri ItemImage;

    public InventoryItemModel(String name, String price, Uri itemImage) {
        Name = name;
        Price = price;
        ItemImage = itemImage;
    }

    public InventoryItemModel() {
    }

    public Uri getItemImage() {
        return ItemImage;
    }

    public void setItemImage(Uri itemImage) {
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
