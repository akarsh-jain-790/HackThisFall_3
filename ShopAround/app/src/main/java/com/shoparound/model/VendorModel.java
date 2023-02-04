package com.shoparound.model;

import java.util.ArrayList;

public class VendorModel {
    private String Email, Gender, Name, Pincode, ProfileImage, ShopImage, ShopName, Category, Address, City, Age;
    private ArrayList<ProductModel> Inventory= new ArrayList<>();

    public VendorModel() {
    }

    public VendorModel(String email, String gender, String name, String pincode, String profileImage,
                       String shopImage, String shopName, String category, String address, String city, String age,
                        ArrayList<ProductModel> inventory) {
        Email = email;
        Gender = gender;
        Name = name;
        Pincode = pincode;
        ProfileImage = profileImage;
        ShopImage = shopImage;
        ShopName = shopName;
        Category = category;
        Address = address;
        City = city;
        Age = age;
        Inventory = inventory;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getShopImage() {
        return ShopImage;
    }

    public void setShopImage(String shopImage) {
        ShopImage = shopImage;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public ArrayList<ProductModel> getInventory() {
        return Inventory;
    }

    public void setInventory(ArrayList<ProductModel> inventory) {
        Inventory = inventory;
    }
}
