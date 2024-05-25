package com.example.planner;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Restaurant {
    String name;
    String location;
    String imageLink;
    String mapLink;
    boolean isAccepted;

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
