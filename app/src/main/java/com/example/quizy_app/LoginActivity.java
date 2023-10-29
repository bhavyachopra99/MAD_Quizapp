package com.example.quizy_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quizy_app.UserDbHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private UserDbHelper userDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UserDbHelper and UI elements
        userDbHelper = new UserDbHelper(this);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
    }

    public void login(View view) {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        // Check if the email and password exist in the user database
        if (userDbHelper.checkCredentials(email, password)) {
            // Successful login
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            // Start the QuizActivity and pass the user's email as an extra in the intent
            Intent intent_start = new Intent(this, QuizActivity.class);
            intent_start.putExtra("user_email", email);
            startActivity(intent_start);
            finish();
        } else {
            // Incorrect email or password
            Toast.makeText(this, "Login failed. Incorrect email or password.", Toast.LENGTH_SHORT).show();
        }
    }

    public void register(View view) {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        // Check if the email already exists in the user database
        if (userDbHelper.checkEmailExists(email)) {
            Toast.makeText(this, "Email already registered. Please log in.", Toast.LENGTH_SHORT).show();
        } else {
            // Email is not registered, so insert the new user into the user database
            if (userDbHelper.insertUser(email, password)) {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
