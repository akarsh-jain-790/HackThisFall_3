package com.shoparound.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SignUpPrefManager {

    Context context;

    public SignUpPrefManager(Context context) {
        this.context = context;
    }

    public void saveUserDetails(String name, String email, String gender, String image, int age) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name", name);
        editor.putString("Email", email);
        editor.putString("Gender", gender);
        editor.putString("ProfileImage", image);
        editor.putInt("Age", age);
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

    public int getAge() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("Age", 0);
    }

    public String getProfileImage() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("ProfileImage", "");
    }

    public boolean checkIfUserDetailsExist() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        boolean isNameEmpty = sharedPreferences.getString("Name", "").isEmpty();
        boolean isEmailEmpty = sharedPreferences.getString("Email", "").isEmpty();
        boolean isGenderEmpty = sharedPreferences.getString("Gender", "").isEmpty();
//        boolean isImageEmpty = sharedPreferences.getString("ProfileImage", "").isEmpty();
        boolean isAgeEmpty = sharedPreferences.getInt("Age", 0) == 0;
        return isNameEmpty || isEmailEmpty || isGenderEmpty || isAgeEmpty;
    }
}
