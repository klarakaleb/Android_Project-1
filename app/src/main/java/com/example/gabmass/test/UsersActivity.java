package com.example.gabmass.test;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.UserHandle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private UserHandler db;
    private EditText username;
    private ListView displayUsers;
    private List<String> users_list;
    public String selectedFromList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        db = new UserHandler(this, null, null, 1);
        db.getReadableDatabase();

        username = (EditText) findViewById(R.id.new_user);

        displayUsers = (ListView) findViewById(R.id.users);
        displayUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                selectedFromList =(String) (displayUsers.getItemAtPosition(myItemInt));
                Intent i = new Intent(getApplicationContext(), QuestionsDisplay.class);
                i.putExtra("user",selectedFromList);
                startActivity(i);
            }

        });

        Button addUser = (Button) findViewById(R.id.add_user);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    User user = new User(username.getText().toString());
                    db.addUser(user);
                    username.setText("");
                    printUsers();

            }
        });

        printUsers();


    }

    public void printUsers() {
        users_list = new ArrayList<>(db.getAllUsers());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                users_list );

        displayUsers.setAdapter(arrayAdapter);

    }
}
