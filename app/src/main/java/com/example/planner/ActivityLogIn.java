package com.example.planner;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.RelativeLayout;
public class ActivityLogIn extends AppCompatActivity {
    Button signInButton, createAccountButton;
    TextView signInText, dontHaveAccountText, passwordText, emailText, wrongPasswordText;
    RelativeLayout createAccountBox, background;
    EditText email;
    com.google.android.material.textfield.TextInputEditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();
    }

    private void initializeViews() {
        signInText = findViewById(R.id.text_sign_in);
        emailText = findViewById(R.id.text_email);
        passwordText = findViewById(R.id.text_pass);
        signInButton = findViewById(R.id.button_sign_in);
        dontHaveAccountText = findViewById(R.id.forgot_account);
        createAccountButton = findViewById(R.id.button_create_account);
        createAccountBox = findViewById(R.id.box_create_account);
        background = findViewById(R.id.background);
        password = findViewById(R.id.pass);
        wrongPasswordText = findViewById(R.id.bledne_haslo);
        email = findViewById(R.id.email);
    }
}
