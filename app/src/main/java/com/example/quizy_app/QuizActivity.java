package com.example.quizy_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
public class QuizActivity extends AppCompatActivity {

    private TextView scoreTextView;
    private TextView questionCountTextView;
    private TextView countdownTextView;
    private TextView questionTextView;
    private LinearLayout checkboxGroup;
    private CheckBox checkboxOption1;
    private CheckBox checkboxOption2;
    private CheckBox checkboxOption3;
    private Button confirmNextButton;

    private List<Question> questionList;
    private int questionCounter;
    private int totalQuestions;
    private Question currentQuestion;
    private int score;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Intent intent = getIntent();
        if (intent != null) {
            userEmail = intent.getStringExtra("user_email");
        }

        scoreTextView = findViewById(R.id.text_view_score);
        questionCountTextView = findViewById(R.id.text_view_question_count);
        countdownTextView = findViewById(R.id.text_view_countdown);
        questionTextView = findViewById(R.id.text_view_question);
        checkboxGroup = findViewById(R.id.checkbox_group);
        checkboxOption1 = findViewById(R.id.checkbox_option1);
        checkboxOption2 = findViewById(R.id.checkbox_option2);
        checkboxOption3 = findViewById(R.id.checkbox_option3);
        confirmNextButton = findViewById(R.id.button_confirm_next);

        QuizDbHelper dbHelper = new QuizDbHelper(this);
        questionList = dbHelper.getAllQuestions();
        totalQuestions = questionList.size();

        showNextQuestion();
    }

    private void showNextQuestion() {
        if (questionCounter < totalQuestions) {
            currentQuestion = questionList.get(questionCounter);

            questionTextView.setText(currentQuestion.getQuestion());
            checkboxOption1.setText(currentQuestion.getOption1());
            checkboxOption2.setText(currentQuestion.getOption2());
            checkboxOption3.setText(currentQuestion.getOption3());

            timeLeftInMillis = 30000;
            startCountDown();

            questionCounter++;
            updateQuestionCount();
        } else {
            UserDbHelper dbHelper = new UserDbHelper(this);
            dbHelper.updateUserScore(userEmail, score);

            finishQuiz();
        }
    }

    private void updateQuestionCount() {
        questionCountTextView.setText("Question: " + questionCounter + "/" + totalQuestions);
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountdownText();
                handleTimeUp();
            }
        }.start();
    }

    private void updateCountdownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        countdownTextView.setText(timeLeftFormatted);
    }

    public void confirmNext(View view) {
        int selectedCount = 0;

        if (checkboxOption1.isChecked()) selectedCount++;
        if (checkboxOption2.isChecked()) selectedCount++;
        if (checkboxOption3.isChecked()) selectedCount++;

        if (selectedCount == 0) {
            Toast.makeText(this, "Please select an answer.", Toast.LENGTH_SHORT).show();
        } else {
            List<Integer> selectedAnswers = new ArrayList<>();
            if (checkboxOption1.isChecked()) {
                selectedAnswers.add(1);
            }
            if (checkboxOption2.isChecked()) {
                selectedAnswers.add(2);
            }
            if (checkboxOption3.isChecked()) {
                selectedAnswers.add(3);
            }

            List<Integer> correctAnswers = currentQuestion.getAnswerNrs();

            if (selectedAnswers.containsAll(correctAnswers) && correctAnswers.containsAll(selectedAnswers)) {
                score++;
                scoreTextView.setText("Score: " + score);
            }

            checkboxOption1.setChecked(false);
            checkboxOption2.setChecked(false);
            checkboxOption3.setChecked(false);
            countDownTimer.cancel();
            showNextQuestion();
        }
    }

    private void finishQuiz() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("USER_SCORE", score);
        intent.putExtra("user_email", userEmail);
        startActivity(intent);
        finish();
    }

    private void handleTimeUp() {
        checkboxOption1.setChecked(false);
        checkboxOption2.setChecked(false);
        checkboxOption3.setChecked(false);
        showNextQuestion();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
