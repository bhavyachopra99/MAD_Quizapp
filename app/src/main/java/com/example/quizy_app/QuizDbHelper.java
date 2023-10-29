package com.example.quizy_app;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Quiz.db";
    private static final int DATABASE_VERSION = 1;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " + QuizContract.QuestionsTable.TABLE_NAME + " ("
                + QuizContract.QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + QuizContract.QuestionsTable.COLUMN_QUESTION + " TEXT, "
                + QuizContract.QuestionsTable.COLUMN_OPTION1 + " TEXT, "
                + QuizContract.QuestionsTable.COLUMN_OPTION2 + " TEXT, "
                + QuizContract.QuestionsTable.COLUMN_OPTION3 + " TEXT, "
                + QuizContract.QuestionsTable.COLUMN_ANSWER_NR + " TEXT)"; // Use TEXT for multiple answers
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuizContract.QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    public long insertQuestion(SQLiteDatabase db, String question, String option1, String option2, String option3, String answerNr) {
        ContentValues values = new ContentValues();
        values.put(QuizContract.QuestionsTable.COLUMN_QUESTION, question);
        values.put(QuizContract.QuestionsTable.COLUMN_OPTION1, option1);
        values.put(QuizContract.QuestionsTable.COLUMN_OPTION2, option2);
        values.put(QuizContract.QuestionsTable.COLUMN_OPTION3, option3);
        values.put(QuizContract.QuestionsTable.COLUMN_ANSWER_NR, answerNr);

        long result = db.insert(QuizContract.QuestionsTable.TABLE_NAME, null, values);
        return result;
    }

    public List<Question> getAllQuestions() {
        List<Question> questionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + QuizContract.QuestionsTable.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String questionText = cursor.getString(cursor.getColumnIndex(QuizContract.QuestionsTable.COLUMN_QUESTION));
                @SuppressLint("Range") String option1 = cursor.getString(cursor.getColumnIndex(QuizContract.QuestionsTable.COLUMN_OPTION1));
                @SuppressLint("Range") String option2 = cursor.getString(cursor.getColumnIndex(QuizContract.QuestionsTable.COLUMN_OPTION2));
                @SuppressLint("Range") String option3 = cursor.getString(cursor.getColumnIndex(QuizContract.QuestionsTable.COLUMN_OPTION3));
                @SuppressLint("Range") String answerNr = cursor.getString(cursor.getColumnIndex(QuizContract.QuestionsTable.COLUMN_ANSWER_NR));

                List<Integer> correctAnswers = convertStringToList(answerNr);

                Question question = new Question(questionText, option1, option2, option3, correctAnswers);
                questionList.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questionList;
    }

    String convertListToString(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (Integer item : list) {
            sb.append(item);
            sb.append(",");
        }
        sb.setLength(sb.length() - 1); // Remove the trailing comma
        return sb.toString();
    }

    private List<Integer> convertStringToList(String str) {
        List<Integer> list = new ArrayList<>();
        String[] items = str.split(",");
        for (String item : items) {
            list.add(Integer.parseInt(item));
        }
        return list;
    }
}
