package com.example.gabmass.test;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by gabmass on 01/01/2017.
 */

public class AttemptHandler extends SQLiteOpenHelper {

    public static final String TAG = "coursework";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "scoresManager";

    private static final String TABLE_ATTEMPTS = "attempts";

    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_SCORE = "score";

    public AttemptHandler(Context context, String Name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ATTEMPTS_TABLE = "CREATE TABLE " + TABLE_ATTEMPTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT,"
                + KEY_SCORE + " INTEGER" +")";
        db.execSQL(CREATE_ATTEMPTS_TABLE);

    }

    public AttemptHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTEMPTS);

        // Create tables again
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }


    public  String getUsername() {
        String username = "";
        Cursor cursor = this.getReadableDatabase().query(
                TABLE_ATTEMPTS, new String[] { KEY_USERNAME },
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                username = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return username;
    }

    public String getScore() {
        String score = "";
        Cursor cursor = this.getReadableDatabase().query(
                TABLE_ATTEMPTS, new String[] { KEY_SCORE },
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                score = cursor.getString(2);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return score;
    }



    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ATTEMPTS, new String[]{KEY_ID,
                        KEY_USERNAME}, KEY_ID + "=?",
                new String[]{username}, null, null, null, null);


        User user = null;

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            user = new User(cursor.getString(0));
            cursor.close();
        }


        return user;
    }

    public void addAttempt(Attempt attempt) {
        SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_USERNAME, attempt.getUserName()); // Username
            values.put(KEY_SCORE, attempt.getScore()); // User score

            // Inserting Row
            db.insert(TABLE_ATTEMPTS, null, values);
            db.close(); // Closing database connection


    }

    //in descending order
    public ArrayList<String> getAllAttempts() {
        ArrayList<String> attemptList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ATTEMPTS + " ORDER BY " + KEY_SCORE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String username =  cursor.getString(cursor.getColumnIndex("username"));
                String score =  cursor.getString(cursor.getColumnIndex("score"));
                attemptList.add(username);
                attemptList.add(score);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return attemptList;
    }




}
