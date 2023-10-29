package com.example.quizy_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizy_app.QuizDbHelper;
import com.example.quizy_app.R;

import java.util.ArrayList;
import java.util.List;

public class QuizBuilder extends AppCompatActivity {
    private EditText editTextQuestion;
    private EditText editTextOption1;
    private EditText editTextOption2;
    private EditText editTextOption3;
    private CheckBox checkBoxOption1;
    private CheckBox checkBoxOption2;
    private CheckBox checkBoxOption3;
    private QuizDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_builder);

        dbHelper = new QuizDbHelper(this);
        editTextQuestion = findViewById(R.id.editTextQuestion);
        editTextOption1 = findViewById(R.id.editTextOption1);
        editTextOption2 = findViewById(R.id.editTextOption2);
        editTextOption3 = findViewById(R.id.editTextOption3);
        checkBoxOption1 = findViewById(R.id.checkBoxOption1);
        checkBoxOption2 = findViewById(R.id.checkBoxOption2);
        checkBoxOption3 = findViewById(R.id.checkBoxOption3);

        Button buttonAddQuiz = findViewById(R.id.buttonAddQuiz);
        buttonAddQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuiz();
            }
        });
    }

    private void addQuiz() {
        String question = editTextQuestion.getText().toString();
        String option1 = editTextOption1.getText().toString();
        String option2 = editTextOption2.getText().toString();
        String option3 = editTextOption3.getText().toString();

        boolean isOption1Correct = checkBoxOption1.isChecked();
        boolean isOption2Correct = checkBoxOption2.isChecked();
        boolean isOption3Correct = checkBoxOption3.isChecked();

        if (question.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else if (!isOption1Correct && !isOption2Correct && !isOption3Correct) {
            Toast.makeText(this, "Select at least one correct option", Toast.LENGTH_SHORT).show();
        } else if ((isOption1Correct ? 1 : 0) + (isOption2Correct ? 1 : 0) + (isOption3Correct ? 1 : 0) > 2) {
            Toast.makeText(this, "Only two options can be correct", Toast.LENGTH_SHORT).show();
        } else {
            List<Integer> correctOptions = new ArrayList<>();
            if (isOption1Correct) correctOptions.add(1);
            if (isOption2Correct) correctOptions.add(2);
            if (isOption3Correct) correctOptions.add(3);

            long result = dbHelper.insertQuestion(dbHelper.getWritableDatabase(),question, option1, option2, option3, dbHelper.convertListToString(correctOptions));
            if (result != -1) {
                Toast.makeText(this, "Question added to the database", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(this, "Error adding question to the database", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void clearFields() {
        editTextQuestion.setText("");
        editTextOption1.setText("");
        editTextOption2.setText("");
        editTextOption3.setText("");
        checkBoxOption1.setChecked(false);
        checkBoxOption2.setChecked(false);
        checkBoxOption3.setChecked(false);
    }
}
