package com.example.quizy_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {

    private String userEmail;
    private int userScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        if (intent != null) {
            userEmail = intent.getStringExtra("user_email");
            userScore = intent.getIntExtra("USER_SCORE", 0);
            Toast.makeText(this, String.valueOf(userScore), Toast.LENGTH_SHORT).show();
        }

        // Display the user's score in a TextView
        TextView textViewScore = findViewById(R.id.text_view_score);
        textViewScore.setText("Your Score: " + userScore);

        // Check and update the highest score if necessary
        updateUserHighScore(userEmail, userScore);
    }

    private void updateUserHighScore(String email, int score) {
        UserDbHelper dbHelper = new UserDbHelper(this);
        int highestScore = dbHelper.getHighestScoreFor(userEmail);

        if (score > highestScore) {
            dbHelper.updateUserScore(email, score);
            Toast.makeText(this, "New highest score!", Toast.LENGTH_SHORT).show();
        }
    }
}
