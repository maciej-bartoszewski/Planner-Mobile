package com.example.planner;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ActivityRestaurants extends AppCompatActivity implements RestaurantsList.RestaurantsListCallback {
    private Button reportButton;
    private ImageButton menuButton;
    private Dialog report;
    private LinearLayout restaurantContainerLayout;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = intent.getParcelableExtra("User");

        setContentView(R.layout.activity_restaurant_view);
        initializeViews();
        initializeReportButton();

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

    private void initializeReportButton() {
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report = new Dialog(ActivityRestaurants.this);
                report.setContentView(R.layout.activity_restaurant_report);
                report.show();
                Objects.requireNonNull(report.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                report.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                initializeSendReportButton(report);
            }
        });
    }

    private void initializeSendReportButton(Dialog report) {
        Button sendReport = report.findViewById(R.id.confirmButton);
        sendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText) (report.findViewById(R.id.editTextName))).getText().toString();
                String location = ((EditText) (report.findViewById(R.id.editTextAddress))).getText().toString();
                String imageLink = ((EditText) (report.findViewById(R.id.editTextImageLink))).getText().toString();
                String mapLink = ((EditText) (report.findViewById(R.id.editTextMapLink))).getText().toString();

                Restaurant restaurant = new Restaurant(name, location, imageLink, mapLink, false);
                if (!restaurant.validateRestaurant()) {
                    Toast.makeText(ActivityRestaurants.this, "Błąd wprowadzonych danych", Toast.LENGTH_SHORT).show();
                    new Vibration(ActivityRestaurants.this);
                } else {
                    restaurant.ifReporterCanReportReport(user, ActivityRestaurants.this);
                    if (report.isShowing()) {
                        report.dismiss();
                    }
                }
            }
        });
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
