package com.example.planner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class ActivityRestaurants extends AppCompatActivity {
    Button reportButton;
    ImageButton menuButton;
    LinearLayout restaurantContainerLayout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = intent.getParcelableExtra("User");

        setContentView(R.layout.activity_restaurant_view);
        initializeViews();
    }

    private void initializeViews() {
        reportButton = findViewById(R.id.reportButton);
        restaurantContainerLayout = findViewById(R.id.restaurantContainerLayout);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        menuButton = findViewById(R.id.menuButton);

        MainNavigationView mainNavigationView = new MainNavigationView();
        mainNavigationView.initializeNavigationView(menuButton, drawerLayout, navigationView, user, ActivityRestaurants.this);
    }
}
