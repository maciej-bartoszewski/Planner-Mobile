package com.example.planner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class ActivityEvents extends AppCompatActivity implements UpcomingEvents.UpcomingEventsCallback {
    Button reportButton;
    ImageButton menuButton;
    LinearLayout eventContainerLayout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = intent.getParcelableExtra("User");

        setContentView(R.layout.activity_event_view);
        initializeViews();

        // displayEvents
        UpcomingEvents upcomingEvents = new UpcomingEvents();
        upcomingEvents.setUpcomingEventsCallback(this);
        upcomingEvents.loadEvents(ActivityEvents.this);
    }

    private void initializeViews() {
        reportButton = findViewById(R.id.reportButton);
        eventContainerLayout = findViewById(R.id.eventContainerLayout);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        menuButton = findViewById(R.id.menuButton);

        MainNavigationView mainNavigationView = new MainNavigationView();
        mainNavigationView.initializeNavigationView(menuButton, drawerLayout, navigationView, user, ActivityEvents.this);
    }

    @Override
    public void displayEvents(List<Event> events) {
        Collections.sort(events);
        for (Event event : events) {
            HashMap<String, Object> eventMap = event.toHashMap();

            // Create event container
            LayoutInflater inflater = LayoutInflater.from(this);
            View newLayout = inflater.inflate(R.layout.activity_event_container, eventContainerLayout, false);

            TextView eventName = newLayout.findViewById(R.id.eventName);
            TextView eventDate = newLayout.findViewById(R.id.eventDate);
            ImageView eventImage = newLayout.findViewById(R.id.eventImage);
            ImageButton eventInfoButton = newLayout.findViewById(R.id.info);

            eventName.setText(Objects.requireNonNull(eventMap.get("Name")).toString());
            eventDate.setText(Objects.requireNonNull(eventMap.get("Date")).toString());
            Picasso.get().load(Objects.requireNonNull(eventMap.get("ImageLink")).toString()).resize(150, 150).transform(new RoundedCornersTransformation(50, 0)).into(eventImage);
            eventInfoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TO DO
                }
            });

            eventContainerLayout.addView(newLayout);
        }
    }
}
