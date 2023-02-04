package com.example.shoparoundsup.util;

import android.content.Context;
import android.content.SharedPreferences;

public class RegShopPreferenceManager {

    Context context;

    public RegShopPreferenceManager(Context context) {
        this.context = context;
    }

    public void saveShopDetails(String shopName, String address, String city, String pincode, String category, String ShopImage) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ShopName", shopName);
        editor.putString("Address", address);
        editor.putString("City", city);
        editor.putString("Pincode", pincode);
        editor.putString("Category", category);
        editor.putString("ShopImage", ShopImage);
        editor.commit();
    }

    public String getshopName() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        // s: data and s1: default value
        return sharedPreferences.getString("ShopName", "");
    }

    public String getaddress() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Address", "");
    }

    public String getcity() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("City", "");
    }

    public String getPincode() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Pincode", "");
    }

    public String getCategory() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Category", "");
    }

    public String getShopImage(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("ShopImage", "");
    }

    public boolean checkIfUserDetailsExist() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        boolean isshopNameEmpty = sharedPreferences.getString("ShopName", "").isEmpty();
        boolean isaddressEmpty = sharedPreferences.getString("Address", "").isEmpty();
        boolean iscityEmpty = sharedPreferences.getString("City", "").isEmpty();
        boolean ispincodeEmpty = sharedPreferences.getString("Pincode", "").isEmpty();
        boolean iscategoryEmpty = sharedPreferences.getString("Category", "").isEmpty();
        boolean isShopImageEmpty = sharedPreferences.getString("ShopImage", "").isEmpty();
        return isshopNameEmpty || isaddressEmpty || iscityEmpty || ispincodeEmpty || iscategoryEmpty || isShopImageEmpty;
    }

}
