package com.example.gabmass.test;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QuestionsDisplay extends AppCompatActivity {

    private static final int REQUEST_KEY_OUTCOME=0;

    private QuestionDb db;
    private ListView displayQuestions;
    private TextView tvResponse;
    private List<String> qList;
    private Button bStart;
    private Button bSubmit;
    private String selectedFromList;
    private int score;
    private AttemptHandler db1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_display);

        final String username = getIntent().getStringExtra("user"); //username that user has chosen in previous activity

        db = new QuestionDb(this);
        db.open();

        db1 = new AttemptHandler(this);
        db1.getReadableDatabase();

        displayQuestions = (ListView) findViewById(R.id.questions);
        displayQuestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                selectedFromList =(String) (displayQuestions.getItemAtPosition(myItemInt));
                Intent i = new Intent(getApplicationContext(), QuizActivity.class);
                i.putExtra("question",selectedFromList);
                i.putExtra("user",username);
                i.putExtra("id",myItemInt);
                startActivityForResult(i, REQUEST_KEY_OUTCOME);


                displayQuestions.getChildAt(myItemInt).setBackgroundColor(
                        Color.parseColor("#689F38")); //to highlight answered questions

            }

        });

        tvResponse = (TextView) findViewById(R.id.result);
        tvResponse.setVisibility(View.INVISIBLE);

        bSubmit = (Button) findViewById(R.id.submit);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResponse.setVisibility(View.VISIBLE);
                tvResponse.setText("YOUR FINAL SCORE IS: " + score + " out of 10");

                Attempt attempt=new Attempt(username,score);
                db1.addAttempt(attempt);

            }
        });
            


        bStart = (Button) findViewById(R.id.start_quiz);
        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), QuizActivity.class);
                i.putExtra("user",username);
                startActivity(i);
            }
        });

        printQuestions();
    }

    //to listen for callbacks
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_KEY_OUTCOME) {
            if (data == null) {
                return;
            }
            String userAnswer = data.getStringExtra("USER_ANSWER");
            if(userAnswer.equals("CORRECT!"))
                score++;
        }
    }


    public void printQuestions() {
        qList = new ArrayList<>(db.getAllQuestions());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                qList );

        displayQuestions.setAdapter(arrayAdapter);

    }
}
