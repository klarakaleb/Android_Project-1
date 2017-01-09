package com.example.gabmass.test;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View.OnClickListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String FIRST_TIME = "firstTime";
    private QuestionDb db;
    private Button start_button;
    private Button b_highscores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Fragment insertion
        Button btnLoad = (Button) findViewById(R.id.btn_load);

//        OnClickListener listener = new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                HelloFragment hello = new HelloFragment();
//                fragmentTransaction.add(R.id.fragment_container, hello, "HELLO");
//                fragmentTransaction.commit();
//            }
//        };

//        btnLoad.setOnClickListener(listener);

        /*
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() { //if app crashes
            @Override
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                System.out.print("Blame the developer");
                System.exit(2);
            }
        });
        */

        // If running app for the first time, parse xml and populate db
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean(FIRST_TIME, false)) { // if the boolean the first time is false is false i.e. if it is the first time
            db = new QuestionDb(this);
            new PopulateDb().execute(); //populate the db from the xml
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(FIRST_TIME, true).commit(); //all changes batched together and copied back to original given that this is the first time app is run
        }



        start_button = (Button) findViewById(R.id.button); //wire up button to a view
        //make it clickable
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent i = new Intent(getApplicationContext(), UsersActivity.class);
                startActivity(i);
            }
        });

        b_highscores =(Button) findViewById(R.id.highscores);
        b_highscores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HighScoresActivity.class);
                startActivity(i);
            }

        } );
    }

    // Performs the background operations of XML parsing and data insertion into db
    private class PopulateDb extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            AssetManager assetManager = getAssets(); //get questions from XML file
            db.open();
            try {
                InputStream inputStream = assetManager.open("data/questions.xml");
                List<Question> questionList = new ArrayList<>(new QuestionHandler()
                        .parse(inputStream));
                //for question in question list
                for (Question q : questionList) {
                    db.createQuestion(q);
                }

            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            db.close();
            return null;
        }



    }
}