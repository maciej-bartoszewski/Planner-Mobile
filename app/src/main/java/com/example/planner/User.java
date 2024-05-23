package com.example.planner;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class User implements Parcelable {

    private String email;
    private String lastName;
    private String name;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;

    }

    protected User(Parcel in) {
        email = in.readString();
        lastName = in.readString();
        name = in.readString();
        password = in.readString();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes("UTF-8"));
            StringBuilder stringBuffer = new StringBuilder();
            for (byte b : hashedBytes) {
                stringBuffer.append(String.format("%02x", b));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException("Could not hash password", e);
        }
    }

    public void login(Context context, TextView wrongPasswordText) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("login").get().addOnCompleteListener(task -> {
            boolean found = false;
            for (QueryDocumentSnapshot document : task.getResult()) {
                Map<String, Object> data = document.getData();
                String email = (String) data.get("Email");
                String password = (String) data.get("Password");
                if (email.equals(this.email) && password.equals(hashPassword(this.password))) {
                    this.name = (String) data.get("Name");
                    this.lastName = (String) data.get("LastName");
                    Intent intent = new Intent(context, ActivityToDo.class);
                    intent.putExtra("User", this);
                    context.startActivity(intent);
                    found = true;
                    break;
                }
            }
            if (!found) {
                wrongPasswordText.setVisibility(View.VISIBLE);
            }
        });
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(lastName);
        dest.writeString(name);
        dest.writeString(password);
    }
}