package com.example.planner;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class ActivityCreateAccount extends AppCompatActivity {
    private EditText name, lastName, email;
    private Button createAccount;
    private TextView message, haveAccount, logIn;
    private com.google.android.material.textfield.TextInputEditText password, reapetedPassword;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean nameFlag=false,lastNameFlag=false,emailFlag=false,passwordFlag=false,passwordRepeatFlag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);

        initializeViews();
        logInOption();
        haveAccountOption();
    }
    private void initializeViews() {
        name = findViewById(R.id.Name);
        lastName = findViewById(R.id.LastName);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        reapetedPassword = findViewById(R.id.RepeatedPassword);
        message = findViewById(R.id.Message);
        createAccount = findViewById(R.id.CreateAccount);
        haveAccount = findViewById(R.id.HaveAccount);
        logIn = findViewById(R.id.LogIn);
    }

    public void logInOption() {
        logIn.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityCreateAccount.this, ActivityLogIn.class);
            startActivity(intent);
        });
    }
    private void haveAccountOption() {
        haveAccount.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityCreateAccount.this, ActivityLogIn.class);
            startActivity(intent);
        });
    }
}