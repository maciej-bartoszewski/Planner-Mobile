package com.example.planner;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class ActivityEvents extends AppCompatActivity implements UpcomingEvents.UpcomingEventsCallback {
    private Button reportButton;
    private ImageButton menuButton;
    private Dialog report;
    private LinearLayout eventContainerLayout;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = intent.getParcelableExtra("User");

        setContentView(R.layout.activity_event_view);
        initializeViews();
        initializeReportButton();

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

    private void initializeReportButton() {
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report = new Dialog(ActivityEvents.this);
                report.setContentView(R.layout.activity_event_report);
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
                String date = ((EditText) (report.findViewById(R.id.editTextData))).getText().toString();
                String location = ((EditText) (report.findViewById(R.id.editTextLocation))).getText().toString();
                String description = ((EditText) (report.findViewById(R.id.editTextDescription))).getText().toString();
                String imageLink = ((EditText) (report.findViewById(R.id.editTextImageLink))).getText().toString();
                String mapLink = ((EditText) (report.findViewById(R.id.editTextMapLink))).getText().toString();

                Event event = new Event(name, description, location, date, imageLink, mapLink, false);
                if (!event.validateEvent()) {
                    Toast.makeText(ActivityEvents.this, "Błąd wprowadzonych danych", Toast.LENGTH_SHORT).show();
                    new Vibration(ActivityEvents.this);
                } else {
                    event.ifReporterCanReportReport(user, ActivityEvents.this);
                    if (report.isShowing()) {
                        report.dismiss();
                    }
                }
            }
        });
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
                    // Create pop up window with more information
                    Dialog info = new Dialog(ActivityEvents.this);
                    info.setContentView(R.layout.activity_event_info);
                    Objects.requireNonNull(info.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    info.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    TextView eventDescription = info.findViewById(R.id.editTextDescription);
                    TextView eventLocation = info.findViewById(R.id.editTextLocation);
                    eventDescription.setText(Objects.requireNonNull(eventMap.get("Description")).toString());
                    eventLocation.setText(Html.fromHtml("<a href=\"" + Objects.requireNonNull(eventMap.get("MapLink")) + "\">" + Objects.requireNonNull(eventMap.get("Location")) + "</a>"));
                    eventLocation.setClickable(true);
                    eventLocation.setMovementMethod(LinkMovementMethod.getInstance());

                    info.show();

                    // Set location under button
                    int[] location = new int[2];
                    eventInfoButton.getLocationOnScreen(location);

                    WindowManager.LayoutParams layoutParams = info.getWindow().getAttributes();
                    layoutParams.gravity = Gravity.TOP | Gravity.START;
                    layoutParams.x = location[0];
                    layoutParams.y = location[1] + eventInfoButton.getHeight();
                    info.getWindow().setAttributes(layoutParams);
                }
            });

            eventContainerLayout.addView(newLayout);
        }
    }
}
