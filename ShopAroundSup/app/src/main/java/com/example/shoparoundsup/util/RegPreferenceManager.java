package com.example.shoparoundsup.util;

import android.content.Context;
import android.content.SharedPreferences;

public class RegPreferenceManager {

    Context context;

    public RegPreferenceManager(Context context) {
        this.context = context;
    }

    public void saveVendorDetails(String name, String email, String gender, String age, String image) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name", name);
        editor.putString("Email", email);
        editor.putString("Gender", gender);
        editor.putString("Age", age);
        editor.putString("ProfileImage", image);
        editor.commit();
    }

    public String getName() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        // s: data and s1: default value
        return sharedPreferences.getString("Name", "");
    }

    public String getEmail() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Email", "");
    }

    public String getGender() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Gender", "");
    }

    public String getAge() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Age", "");
    }

    public String getProfileImage(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("ProfileImage", "");
    }


    public boolean checkIfUserDetailsExist() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        boolean isNameEmpty = sharedPreferences.getString("Name", "").isEmpty();
        boolean isEmailEmpty = sharedPreferences.getString("Email", "").isEmpty();
        boolean isGenderEmpty = sharedPreferences.getString("Gender", "").isEmpty();
        boolean isAgeEmpty = sharedPreferences.getString("Age", "").isEmpty();
        boolean isImageEmpty = sharedPreferences.getString("ProfileImage", "").isEmpty();
        return isNameEmpty || isEmailEmpty || isGenderEmpty || isAgeEmpty || isImageEmpty;
    }
}

