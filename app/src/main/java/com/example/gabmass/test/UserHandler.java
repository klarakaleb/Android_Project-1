package com.example.gabmass.test;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabmass on 01/01/2017.
 */

public class UserHandler extends SQLiteOpenHelper {

    public static final String TAG = "coursework";
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "usersManager";

    public static final String TABLE_USERS = "users";

    public static final String KEY_ID = "id";
    public static final String KEY_USERNAME = "username";

    public UserHandler(Context context, String Name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT" + ");";
        db.execSQL(CREATE_USERS_TABLE);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }


    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        boolean USER_NOT_ADDED = !doesUserExist(user.getUserName());

        if (USER_NOT_ADDED) {
            ContentValues values = new ContentValues();
            values.put(KEY_USERNAME, user.getUserName());
            db.insert(TABLE_USERS, null, values);
            db.close();
        } else {
            Log.d(TAG, "addUser: " + user.getUserName() + " already exists in the database.");
        }


    }

    private boolean doesUserExist(String username) {
        return !(getUser(username) == null);
    }

    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        // It's a good practice to use parameter ?, instead of concatenate string
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID,
                        KEY_USERNAME}, KEY_ID + "=?",
                new String[]{username}, null, null, null, null);


        User user = null;

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            user = new User(cursor.getString(1));
            cursor.close(); // Closing database connection
        }


        return user;
    }

    public ArrayList<String> getAllUsers() {

        ArrayList<String> userList = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;
        //Open connection to read only
        SQLiteDatabase db = this.getWritableDatabase();

        // looping through all rows and adding to list
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String username =  cursor.getString(cursor.getColumnIndex("username"));
                userList.add(username);
            } while (cursor.moveToNext());
        }


        return userList;

    }


    public  String getUsername() {
        String username = "";
        Cursor cursor = this.getReadableDatabase().query(
                TABLE_USERS, new String[] { KEY_USERNAME },
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                username = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return username;
    }






}
