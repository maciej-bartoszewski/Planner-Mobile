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

public class RestaurantsList {
    private final List<Restaurant> restaurants = new Vector<>();
    private RestaurantsListCallback restaurantsListCallback;

    public void loadRestaurants(Context parent) {
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        dataBase.collection("restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                if ((boolean) data.get("IsAccepted")) {
                                    restaurants.add(new Restaurant(data));
                                }
                            }
                        } else {
                            Toast.makeText(parent, "Błąd wyczytywania restauracji", Toast.LENGTH_SHORT).show();
                        }
                        restaurantsListCallback.displayRestaurants(restaurants);
                    }
                });
    }

    public interface RestaurantsListCallback {
        void displayRestaurants(List<Restaurant> restaurants);
    }

    public void setRestaurantsListCallback(RestaurantsListCallback restaurantsListCallback) {
        this.restaurantsListCallback = restaurantsListCallback;
    }
}
