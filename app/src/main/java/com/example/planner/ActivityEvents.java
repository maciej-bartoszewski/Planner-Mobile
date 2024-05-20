package com.example.planner;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class ActivityEvents extends AppCompatActivity {
    Button reportButton;
    ImageButton menuButton;
    LinearLayout eventContainerLayout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        initializeViews();
    }

    private void initializeViews() {
        reportButton = findViewById(R.id.reportButton);
        eventContainerLayout = findViewById(R.id.eventContainerLayout);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        menuButton = findViewById(R.id.menuButton);
    }

}
