package com.example.planner;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

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
        showInvalidMessage();
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
    private void showInvalidMessage() {
        //name handle
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nameInput = s.toString();
                if (nameInput.isEmpty()) {
                    message.setText("Brak Imienia!");
                    message.setVisibility(View.VISIBLE);
                    nameFlag = false;
                } else {
                    message.setVisibility(View.INVISIBLE);
                    nameFlag = true;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String lastNameInput = s.toString();
                if (lastNameInput.isEmpty()) {
                    message.setText("Brak Nazwiska!");
                    message.setVisibility(View.VISIBLE);
                    lastNameFlag = false;
                } else {
                    message.setVisibility(View.INVISIBLE);
                    lastNameFlag = true;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String emailInput = s.toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    message.setText("Błędny email!");
                    message.setVisibility(View.VISIBLE);
                    emailFlag = false;
                } else {
                    db.collection("login").get().addOnCompleteListener(task -> {
                        emailFlag = true;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> data = document.getData();
                            if (data.get("Email").equals(emailInput)) {
                                message.setText("Podany email już istnieje");
                                message.setVisibility(View.VISIBLE);
                                emailFlag = false;
                                break;
                            }
                        }
                        if (emailFlag) {
                            message.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String passwordInput = s.toString();
                if (passwordInput.length() < 8 || !passwordInput.matches(".*[!@#$%^&*()~`=_+].*")) {
                    message.setText("Hasło musi składać się z 8 znaków i znaku specjalnego!");
                    message.setVisibility(View.VISIBLE);
                    passwordFlag = false;
                } else {
                    message.setVisibility(View.INVISIBLE);
                    passwordFlag = true;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        reapetedPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String passwordInput = password.getText().toString();
                String repeatedPasswordInput = s.toString();
                if (!passwordInput.equals(repeatedPasswordInput) || repeatedPasswordInput.isEmpty()) {
                    message.setText("Hasła nie są takie same!");
                    message.setVisibility(View.VISIBLE);
                    passwordRepeatFlag = false;
                } else {
                    message.setVisibility(View.INVISIBLE);
                    passwordRepeatFlag = true;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        createAccount.setOnClickListener(v -> {
            if (lastNameFlag && nameFlag && emailFlag && passwordFlag && passwordRepeatFlag &&!email.getText().toString().isEmpty()) {
                message.setVisibility(View.INVISIBLE);
                Map<String, Object> createAccount = new HashMap<>();
                createAccount.put("Email", email.getText().toString());
                createAccount.put("Name", name.getText().toString());
                createAccount.put("LastName", lastName.getText().toString());
                createAccount.put("Password", hashPassword(password.getText().toString()));
                db.collection("login")
                        .add(createAccount)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(ActivityCreateAccount.this, "Założono konto!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ActivityCreateAccount.this, "Problem z baza danych (Przepraszamy)", Toast.LENGTH_SHORT).show();
                            }
                        });
                Intent intent = new Intent(ActivityCreateAccount.this, ActivityLogIn.class);
                startActivity(intent);
            } else {
                if (!nameFlag) {
                    message.setText("Brak Imienia!\n");
                }
                else if (!lastNameFlag) {
                    message.setText("Brak Nazwiska!\n");
                }
                else if (!passwordFlag) {
                    message.setText("Hasło musi składać sie z 8 znaków i znaku specjalnego!\n");
                }
                else if (!passwordRepeatFlag) {
                    message.setText("Hasła nie są takie same!\n");
                }
                else {
                    message.setText("Niepoprawny email/email jest w uzyciu\n");
                }
                message.setVisibility(View.VISIBLE);
            }
        });
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