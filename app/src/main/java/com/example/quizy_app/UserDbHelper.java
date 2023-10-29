package com.example.quizy_app;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "User.db";
    private static final int DATABASE_VERSION = 1;

    // Define the user table and its columns
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_SCORE = "score";

    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the user table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_SCORE + " INTEGER DEFAULT 0)";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the user table if it exists and recreate it
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Add methods for user database operations
    public boolean insertUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_SCORE, 0); // Initialize score to 0

        long newRowId = db.insert(TABLE_USERS, null, values);

        return newRowId != -1;
    }

    public boolean checkCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        boolean loginSuccessful = cursor.getCount() > 0;

        cursor.close();

        return loginSuccessful;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        boolean emailExists = cursor.getCount() > 0;

        cursor.close();

        return emailExists;
    }

    public int getHighestScoreFor(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_SCORE};
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {userEmail};
        String orderBy = COLUMN_SCORE + " DESC";

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, orderBy);

        int highestScore = 0;

        if (cursor.moveToFirst()) {
            highestScore = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE));
        }

        cursor.close();

        return highestScore;
    }

    @SuppressLint("Range")
    public int getHighestScore() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_SCORE};
        String orderBy = COLUMN_SCORE + " DESC";
        Cursor cursor = db.query(TABLE_USERS, columns, null, null, null, null, orderBy);

        int highestScore = 0;

        if (cursor.moveToFirst()) {
            highestScore = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE));
        }

        cursor.close();

        return highestScore;
    }

    public void updateUserScore(String userEmail, int newScore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SCORE, newScore);

        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { userEmail };

        int updatedRows = db.update(TABLE_USERS, values, selection, selectionArgs);

    }

}
