package com.example.planner;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ActivityToDo extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView textData, goodMorning;

    Button addButton;
    User user;
    ImageButton menuButton;
    Dialog addNotePopUp;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    LinearLayout containerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = intent.getParcelableExtra("User");
        setContentView(R.layout.activity_todo_view);
        initializeViews();
        setTxtDate();
        setGreeting();
    }

    private void initializeViews() {
        navigationView = findViewById(R.id.navigationView);
        menuButton = findViewById(R.id.menuButton);
        containerLayout = findViewById(R.id.containerLayout);
        drawerLayout = findViewById(R.id.drawerLayout);
        addButton = findViewById(R.id.addButton);
        goodMorning = findViewById(R.id.goodMorning);
        textData = findViewById(R.id.textData);
    }
    private void setGreeting() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        String greeting;
        if (hourOfDay >= 6 && hourOfDay < 18) {
            greeting = "Dzień dobry";
        } else {
            greeting = "Dobry wieczór";
        }
        goodMorning.setText(greeting);
    }

    private void setTxtDate() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Pobierz dzien tygodnia
        SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE", new Locale("pl", "PL"));
        String dayOfWeek = dayOfWeekFormat.format(currentDate);

        // Pobierz dzien miesiaca
        SimpleDateFormat dayOfMonthFormat = new SimpleDateFormat("dd", new Locale("pl", "PL"));
        String dayOfMonth = dayOfMonthFormat.format(currentDate);

        // Pobierz miesiac
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", new Locale("pl", "PL"));
        String month = monthFormat.format(currentDate);

        // Pobierz rok
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", new Locale("pl", "PL"));
        String year = yearFormat.format(currentDate);

        String formattedDate = String.format("%s, %s %s %s", dayOfWeek, dayOfMonth, month, year);

        textData.setText(formattedDate);
    }
}
