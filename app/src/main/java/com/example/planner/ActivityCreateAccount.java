package com.example.planner;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityCreateAccount extends AppCompatActivity {
    EditText name, lastName, email;
    Button createAccount;
    TextView message, haveAccount, logIn;
    com.google.android.material.textfield.TextInputEditText password, reapetedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);

        initializeViews();
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
}