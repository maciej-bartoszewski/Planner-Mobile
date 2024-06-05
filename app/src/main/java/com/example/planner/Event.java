package com.example.planner;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

public class Event implements Comparable {
    private final String name;
    private final String date;
    private final String location;
    private final String description;
    private final String imageLink;
    private final String mapLink;
    private final boolean isAccepted;

    public Event(String name, String description, String location, String date, String imageLink, String mapLink, boolean isAccepted) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.date = date;
        this.imageLink = imageLink;
        this.mapLink = mapLink;
        this.isAccepted = isAccepted;
    }

    public Event(Map<String, Object> eventMap) {
        name = Objects.requireNonNull(eventMap.get("Name")).toString();
        date = Objects.requireNonNull(eventMap.get("Date")).toString();
        location = Objects.requireNonNull(eventMap.get("Location")).toString();
        description = Objects.requireNonNull(eventMap.get("Description")).toString();
        imageLink = Objects.requireNonNull(eventMap.get("ImageLink")).toString();
        mapLink = Objects.requireNonNull(eventMap.get("MapLink")).toString();
        isAccepted = ((boolean) eventMap.get("IsAccepted"));
    }

    public void ifReporterCanReportReport(User reporter, Context parent) {
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();

        // Check can reporter report
        dataBase.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean canHe = true;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                if (data.get("Reporter").equals(reporter.getEmail()) && !(boolean) data.get("IsAccepted")) {
                                    Toast.makeText(parent, "Twoje poprzednie zgłoszenie\njest nadal przetwarzane", Toast.LENGTH_LONG).show();
                                    canHe = false;
                                    break;
                                }
                            }
                            if (canHe) {
                                reportEvent(reporter, parent);
                            }else {
                                new Vibration(parent);
                            }
                        }
                    }
                });
    }

    private void reportEvent(User reporter, Context parent) {
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        HashMap<String, Object> eventMap = this.toHashMap();
        eventMap.put("Reporter", reporter.getEmail());

        dataBase.collection("events")
                .add(eventMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(parent, "Wysłano zgłoszenie", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(parent, "Błąd wysyłania zgłoszenia", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public boolean validateEvent() {
        String[] attributes = {"Name", "Date", "Location", "Description", "ImageLink", "MapLink", "IsAccepted"};
        for (String attribute : attributes) {
            if (this.toHashMap().get(attribute) == null) {
                return false;
            }
            if (Objects.requireNonNull(this.toHashMap().get(attribute)).toString().isEmpty()) {
                return false;
            }
            if (attribute.equals("Date")) {
                if (!Objects.requireNonNull(this.toHashMap().get(attribute)).toString().matches("\\d{2}.\\d{2}.\\d{4} \\d{2}:\\d{2}$")) {
                    return false;
                }
                if (!validateDate(Objects.requireNonNull(this.toHashMap().get(attribute)).toString())) {
                    return false;
                }
            }
        }
        return true;
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> eventMap = new HashMap<>();

        eventMap.put("Name", name);
        eventMap.put("Date", date);
        eventMap.put("Location", location);
        eventMap.put("Description", description);
        eventMap.put("ImageLink", imageLink);
        eventMap.put("MapLink", mapLink);
        eventMap.put("IsAccepted", isAccepted);

        return eventMap;
    }

    private boolean validateDate(String date) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            try {
                LocalDateTime.parse(date, formatter);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int compareTo(Object other) {
        String[] otherStrings = Objects.requireNonNull(((Event) other).toHashMap().get("Date")).toString().split(" |:|\\.");
        String[] thisStrings = Objects.requireNonNull(this.toHashMap().get("Date")).toString().split(" |:|\\.");

        Vector<Integer> otherDateIntegers = new Vector<>();
        Vector<Integer> thisDateIntegers = new Vector<>();

        for (String s : otherStrings) {
            otherDateIntegers.add(Integer.parseInt(s));
        }
        for (String s : thisStrings) {
            thisDateIntegers.add(Integer.parseInt(s));
        }

        Date otherDate = new Date(otherDateIntegers.get(2), otherDateIntegers.get(1), otherDateIntegers.get(0), otherDateIntegers.get(3), otherDateIntegers.get(4));
        Date thisDate = new Date(thisDateIntegers.get(2), thisDateIntegers.get(1), thisDateIntegers.get(0), thisDateIntegers.get(3), thisDateIntegers.get(4));

        return thisDate.compareTo(otherDate);
    }
}
