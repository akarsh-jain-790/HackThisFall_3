package com.example.shoparoundsup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends AppCompatActivity {
    AnimatedBottomBar animatedBottomBar;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animatedBottomBar = findViewById(R.id.animatedBottomBar);

        if(savedInstanceState == null){
            animatedBottomBar.selectTabById(R.id.tab_home,true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container_animated, homeFragment).commit();
        }
        animatedBottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int newIndex, @NotNull AnimatedBottomBar.Tab newTab) {
                Fragment fragment = null;
                switch(newTab.getId()){
                    case R.id.tab_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.tab_inventory:
                        fragment = new InventoryFragment();
                        break;
                    case R.id.tab_notification:
                        fragment = new NotificationFragment();
                        break;
                    case R.id.tab_profile:
                        fragment = new ProfileFragment();
                        break;
                }
                if(fragment != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container_animated, fragment).commit();
                }else{
                    Toast.makeText(MainActivity.this, "Error in creating Fragments", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTabReselected(int i, @NotNull AnimatedBottomBar.Tab tab) {

            }
        });
    }
}
