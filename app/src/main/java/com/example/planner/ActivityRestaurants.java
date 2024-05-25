package com.example.planner;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ActivityRestaurants extends AppCompatActivity implements RestaurantsList.RestaurantsListCallback {
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

        // displayRestaurants
        RestaurantsList restaurantsList = new RestaurantsList();
        restaurantsList.setRestaurantsListCallback(this);
        restaurantsList.loadRestaurants(ActivityRestaurants.this);
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

    @Override
    public void displayRestaurants(List<Restaurant> restaurants) {
        for (Restaurant restaurant : restaurants) {
            HashMap<String, Object> restaurantMap = restaurant.toHashMap();

            LayoutInflater inflater = LayoutInflater.from(this);
            View newLayout = inflater.inflate(R.layout.activity_restaurant_container, restaurantContainerLayout, false);

            TextView restaurantName = newLayout.findViewById(R.id.restaurantName);
            TextView restaurantLocation = newLayout.findViewById(R.id.restaurantLocation);
            ImageView restaurantImage = newLayout.findViewById(R.id.restaurantImage);

            restaurantName.setText(Objects.requireNonNull(restaurantMap.get("Name")).toString());
            restaurantLocation.setText(Html.fromHtml("<a href=\"" +
                    Objects.requireNonNull(restaurantMap.get("MapLink")) + "\">" +
                    Objects.requireNonNull(restaurantMap.get("Location")) + "</a>"));
            restaurantLocation.setClickable(true);
            restaurantLocation.setMovementMethod(LinkMovementMethod.getInstance());
            Picasso.get().load(Objects.requireNonNull(restaurantMap.get("ImageLink")).toString()).resize(150, 150).transform(new RoundedCornersTransformation(50, 0)).into(restaurantImage);

            restaurantContainerLayout.addView(newLayout);
        }
    }
}
