package com.example.planner;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class MainNavigationView {
    public void initializeNavigationView(ImageButton menuButton, DrawerLayout drawerLayout, NavigationView navigationView, User user, Context parent) {
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
                Calendar calendar = Calendar.getInstance();
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                String toDisplay;
                if (hourOfDay >= 6 && hourOfDay < 18) {
                    toDisplay = "Dzień dobry\n" + user.getName();
                } else {
                    toDisplay = "Dobry wieczór\n" + user.getName();
                }
                ((TextView) (navigationView.findViewById(R.id.textUserName))).setText(toDisplay);
                ((TextView) (navigationView.findViewById(R.id.textUserEmail))).setText(user.getEmail());
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.todo) {
                    Intent intent = new Intent(parent, ActivityToDo.class);
                    intent.putExtra("User", user);
                    parent.startActivity(intent);
                } else if (itemId == R.id.Events) {
                    Intent intent = new Intent(parent, ActivityEvents.class);
                    intent.putExtra("User", user);
                    parent.startActivity(intent);
                } else if (itemId == R.id.Restaurants) {
                    Intent intent = new Intent(parent, ActivityRestaurants.class);
                    intent.putExtra("User", user);
                    parent.startActivity(intent);
                } else if (itemId == R.id.LogOut) {
                    Intent intent = new Intent(parent, ActivityLogIn.class);
                    intent.putExtra("User", user);
                    parent.startActivity(intent);
                }
                drawerLayout.close();
                return false;
            }
        });
    }
}
