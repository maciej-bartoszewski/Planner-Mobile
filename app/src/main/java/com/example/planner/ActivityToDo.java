package com.example.planner;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class ActivityToDo extends AppCompatActivity {

    Button addButton;
    ImageButton menuButton;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    LinearLayout containerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_view);
        initializeViews();
    }

    private void initializeViews() {
        navigationView = findViewById(R.id.navigationView);
        menuButton = findViewById(R.id.menuButton);
        containerLayout = findViewById(R.id.containerLayout);
        drawerLayout = findViewById(R.id.drawerLayout);
        addButton = findViewById(R.id.addButton);
    }
}
