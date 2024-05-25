package com.example.planner;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
        initializeaddNoteButton();

    }

    private void initializeViews() {
        navigationView = findViewById(R.id.navigationView);
        menuButton = findViewById(R.id.menuButton);
        containerLayout = findViewById(R.id.containerLayout);
        drawerLayout = findViewById(R.id.drawerLayout);
        addButton = findViewById(R.id.addButton);
        goodMorning = findViewById(R.id.goodMorning);
        textData = findViewById(R.id.textData);

        MainNavigationView mainNavigationView = new MainNavigationView();
        mainNavigationView.initializeNavigationView(menuButton, drawerLayout, navigationView, user, ActivityToDo.this);
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
    private void initializeaddNoteButton() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotePopUp = new Dialog(ActivityToDo.this);
                addNotePopUp.setContentView(R.layout.activity_todo_pop_up);
                addNotePopUp.show();

                addNotePopUp.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                addNotePopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                initializeSendaddNoteButton(addNotePopUp);
            }
        });
    }
    public static boolean isValidDate(String dateString) {
        if (dateString == null || !dateString.matches("\\d{2}/\\d{2}/\\d{4}")) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(dateString);
            return date != null;
        } catch (ParseException e) {
            return false;
        }
    }
    private void initializeSendaddNoteButton(Dialog addNotePopUp) {
        Button addNote = addNotePopUp.findViewById(R.id.confirmButton);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText title = addNotePopUp.findViewById(R.id.editTextTitle);
                EditText date = addNotePopUp.findViewById(R.id.editTextdate);
                TextView invalidDate = addNotePopUp.findViewById(R.id.invalidDate);
                EditText description = addNotePopUp.findViewById(R.id.EditTextDescription);

                if (title.getText().toString().isEmpty() || description.getText().toString().isEmpty()) {
                    invalidDate.setText("Brak tytułu/opisu");
                    invalidDate.setVisibility(View.VISIBLE);
                } else {
                    invalidDate.setVisibility(View.INVISIBLE);

                    if (isValidDate(date.getText().toString())) {
                        Map<String, Object> note = new HashMap<>();
                        //notatka
                        note.put("title", title.getText().toString());
                        note.put("date", date.getText().toString());
                        note.put("description", description.getText().toString());
                        note.put("email", user.getEmail().toString());

                        db.collection("toDoList").add(note).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(ActivityToDo.this, "Dodano notatke", Toast.LENGTH_SHORT).show();
                                containerLayout.removeAllViews();
//                                displayNotes();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ActivityToDo.this, "blad dodawania notatki", Toast.LENGTH_SHORT).show();
                            }
                        });
                        if (addNotePopUp.isShowing()) {
                            addNotePopUp.dismiss();
                        }
                    } else {
                        invalidDate.setText("Niepoprawna data (Format DD/MM/RR)");
                        invalidDate.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

}
