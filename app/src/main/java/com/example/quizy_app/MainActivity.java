package com.example.quizy_app;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView highScoreTextView;
    private UserDbHelper userDbHelper;
    private QuizDbHelper quizDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        highScoreTextView = findViewById(R.id.text_view_highscore);
        userDbHelper = new UserDbHelper(this);
        quizDbHelper = new QuizDbHelper(this);

        updateHighScoreText();
    }
    public void startQuiz(View view) {
        // Navigate to the LoginActivity to start the quiz
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToQuizBuilder(View view) {
        // Navigate to the QuizBuilder activity to add questions
        Intent intent = new Intent(MainActivity.this, QuizBuilder.class);
        startActivity(intent);
    }

    private void updateHighScoreText() {
        // Fetch the highest score from the user database
        int highestScore = userDbHelper.getHighestScore();
        highScoreTextView.setText("Highscore: " + highestScore);
    }

}