package com.example.planner;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;
import java.util.Vector;

public class UpcomingEvents {
    private final List<Event> events = new Vector<>();
    UpcomingEventsCallback eventsCallback;

    public void loadEvents(Context parent) {
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        dataBase.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                if ((boolean) data.get("IsAccepted")) {
                                    events.add(new Event(data));
                                }
                            }
                        } else {
                            Toast.makeText(parent, "Błąd wyczytywania wydarzeń", Toast.LENGTH_SHORT).show();
                        }
                        eventsCallback.displayEvents(events);
                    }
                });
    }

    public interface UpcomingEventsCallback {
        void displayEvents(List<Event> events);
    }

    public void setUpcomingEventsCallback(UpcomingEventsCallback eventsCallback) {
        this.eventsCallback = eventsCallback;
    }

}
