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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Restaurant {
    private final String name;
    private final String location;
    private final String imageLink;
    private final String mapLink;
    private final boolean isAccepted;

    public Restaurant(String name, String location, String imageLink, String mapLink, boolean isAccepted) {
        this.name = name;
        this.location = location;
        this.imageLink = imageLink;
        this.mapLink = mapLink;
        this.isAccepted = isAccepted;
    }

    public Restaurant(Map<String, Object> restaurantMap) {
        name = Objects.requireNonNull(restaurantMap.get("Name")).toString();
        location = Objects.requireNonNull(restaurantMap.get("Location")).toString();
        imageLink = Objects.requireNonNull(restaurantMap.get("ImageLink")).toString();
        mapLink = Objects.requireNonNull(restaurantMap.get("MapLink")).toString();
        isAccepted = ((boolean) restaurantMap.get("IsAccepted"));
    }

    public void ifReporterCanReportReport(User reporter, Context parent) {
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();

        // Check can reporter report
        dataBase.collection("restaurants")
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
                                reportRestaurant(reporter, parent);
                            }
                        }
                    }
                });
    }

    private void reportRestaurant(User reporter, Context parent) {
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        HashMap<String, Object> restaurantMap = this.toHashMap();
        restaurantMap.put("Reporter", reporter.getEmail());

        dataBase.collection("restaurants")
                .add(restaurantMap)
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

    public boolean validateRestaurant() {
        String[] attributes = {"Name", "Location", "ImageLink", "MapLink", "IsAccepted"};
        for (String attribute : attributes) {
            if (this.toHashMap().get(attribute) == null) {
                return false;
            }
            if (Objects.requireNonNull(this.toHashMap().get(attribute)).toString().isEmpty()) {
                return false;
            }
        }
        return true;
    }


    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> restaurantMap = new HashMap<>();

        restaurantMap.put("Name", name);
        restaurantMap.put("Location", location);
        restaurantMap.put("ImageLink", imageLink);
        restaurantMap.put("MapLink", mapLink);
        restaurantMap.put("IsAccepted", isAccepted);

        return restaurantMap;
    }
}
