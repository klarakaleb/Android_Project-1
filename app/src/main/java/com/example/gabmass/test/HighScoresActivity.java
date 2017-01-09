package com.example.gabmass.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class HighScoresActivity extends AppCompatActivity {

    AttemptHandler db;
    ListView displayScores;
    List<String> scores_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        db = new AttemptHandler(this, null, null, 1);
        db.getReadableDatabase();

        displayScores = (ListView) findViewById(R.id.highscores_ls);

        printHighScores();
    }

    public void printHighScores(){
        scores_list = new ArrayList<>(db.getAllAttempts());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                scores_list );

        displayScores.setAdapter(arrayAdapter);
    }
}
