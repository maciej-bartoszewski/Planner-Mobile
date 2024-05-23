package com.example.planner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.EditText;

public class ActivityLogIn extends AppCompatActivity {
    private Button signInButton, createAccountButton;
    private TextView wrongPasswordText;
    private EditText email;
    private com.google.android.material.textfield.TextInputEditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();
        wrongPasswordText.setVisibility(View.INVISIBLE);
        email.setText("");
        password.setText("");
        showErrors();
        goToCreateAccountPage();
    }

    private void goToCreateAccountPage() {
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogIn.this, ActivityCreateAccount.class);
                startActivity(intent);
            }
        });
    }

    private void showErrors() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = email.getText().toString();
                String passwordInput = password.getText().toString().trim();
                User user =new User(emailInput, passwordInput);
                user.login(ActivityLogIn.this,wrongPasswordText);
            }
        });
    }
    private void initializeViews() {
        signInButton = findViewById(R.id.button_sign_in);
        createAccountButton = findViewById(R.id.button_create_account);
        password = findViewById(R.id.pass);
        wrongPasswordText = findViewById(R.id.bledne_haslo);
        email = findViewById(R.id.email);
    }
}