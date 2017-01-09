package com.example.gabmass.test;

/**
 * Created by gabmass on 30/12/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;



/**
 * QuizActivity.java
 *
 */
public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    private final Handler handler = new Handler();
    private TextView tvNumber, tvQuestion, tvResponse;
    private Button button1, button2, button3, button4, b_restart, b_cheat;
    private Button b_skip,b_highscores;
    private QuestionDb db;
    private AttemptHandler db1;
    private List<Question> qList;
    private Question currentQ;
    private int index;
    private int score;
    private int qPoints;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        db1 = new AttemptHandler(this, null, null, 1);
        setupUI(); //wire values to views

        if(!getIntent().getExtras().containsKey("question")) {
            startQuiz(); //get questions from the questions list one at a time

        } else {
            int id = getIntent().getIntExtra("id",0);
            setAQuestion(id);

        }

    }



    private void startQuiz() {

        getQuestions(); //open question database

        index = 0;
        score = 0;
        qPoints = 1;


        //views made invisible until needed
        b_restart.setVisibility(View.INVISIBLE);
        b_highscores.setVisibility(View.INVISIBLE);
        tvResponse.setVisibility(View.INVISIBLE);

        // Set the view for each question
        setQuestionView();
    }

    private void setAQuestion(int qIndex){
        getQuestions();
        enableButtons();

        b_restart.setVisibility(View.INVISIBLE);
        b_highscores.setVisibility(View.INVISIBLE);
        tvResponse.setVisibility(View.INVISIBLE);

        qPoints = 1;
        currentQ = qList.get(qIndex);
        int qId = qIndex + 1;

        tvResponse.setVisibility(View.INVISIBLE);

        tvNumber.setText("Question " + qId + "/" + qList.size()); // to monitor users progress through the quiz

        //set relevant texts to each view
        tvQuestion.setText(currentQ.getText());
        button1.setText(currentQ.getChoiceList().get(0));
        button2.setText(currentQ.getChoiceList().get(1));
        button3.setText(currentQ.getChoiceList().get(2));
        button4.setText(currentQ.getChoiceList().get(3));

        //cheat button clickable
        b_cheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score-=1;
                tvResponse.setVisibility(View.VISIBLE);
                tvResponse.setText(currentQ.getAnswer().toUpperCase());

            }

        } );

        //no points for cheaters/skippers
        b_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score+=0;
                setQuestionView();
            }

        } );

        b_highscores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HighScoresActivity.class);
                startActivity(i);
            }

        } );


    }

    private void setQuestionView() {

        if (index < qList.size()) {
            enableButtons();
            qPoints = 1;
            currentQ = qList.get(index);
            int qId = index + 1;

            tvResponse.setVisibility(View.INVISIBLE);


            tvNumber.setText("Question " + qId + "/" + qList.size()); // to monitor users progress through the quiz

            //set relevant texts to each view
            tvQuestion.setText(currentQ.getText());
            button1.setText(currentQ.getChoiceList().get(0));
            button2.setText(currentQ.getChoiceList().get(1));
            button3.setText(currentQ.getChoiceList().get(2));
            button4.setText(currentQ.getChoiceList().get(3));

            //cheat button clickable
            b_cheat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    score-=1;
                    tvResponse.setVisibility(View.VISIBLE);
                    tvResponse.setText(currentQ.getAnswer().toUpperCase());

                }

            } );

            //no points for cheaters/skippers
            b_skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    score+=0;
                    setQuestionView();
                }

            } );

            b_highscores.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), HighScoresActivity.class);
                    startActivity(i);
                }

            } );

            index++; // quiz progression

        } else {
            b_restart.setVisibility(View.VISIBLE);
            b_highscores.setVisibility(View.VISIBLE);

            tvResponse.setText("YOUR FINAL SCORE IS: " + score + " out of 10");
            b_restart.setText("Restart");
            b_highscores.setText("SCORES");
            disableButtons();

            String username = getIntent().getStringExtra("user");
            Attempt attempt = new Attempt(username, score);
            db1.addAttempt(attempt);
        }



    }



    @Override
    public void onClick(View v) {

        Button button = (Button) findViewById(v.getId());

        if (button.getText().equals("Restart")) { //if restart button clicked, new activity intent created
            Intent i = new Intent(getApplicationContext(), UsersActivity.class);
            startActivity(i);
        } else if (currentQ.getAnswer().equals(button.getText()) ) {
            score = score + qPoints;
            tvResponse.setVisibility(View.VISIBLE);
            tvResponse.setText("CORRECT!");
            if(!getIntent().getExtras().containsKey("question")){handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setQuestionView();
                }
            }, 1000);}
            Intent data = new Intent();
            data.putExtra("USER_ANSWER", tvResponse.getText());
            setResult(RESULT_OK, data);
            disableButtons();//next question displayed after some time has passed

        } else {
            score = score +0;
            tvResponse.setVisibility(View.VISIBLE);
            tvResponse.setText("INCORRECT!");
            if(!getIntent().getExtras().containsKey("question")){handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setQuestionView();
                }
            }, 1000);}
            Intent data = new Intent();
            data.putExtra("USER_ANSWER", tvResponse.getText());
            setResult(RESULT_OK, data);
            disableButtons();

        }

        Intent i = new Intent();


    }



    private void getQuestions() {

        db.open();
        qList = new ArrayList<>(db.getQuestions());
        db.close();
    }

    public void setupUI() {

        db = new QuestionDb(this);
        tvNumber = (TextView) findViewById(R.id.tv_number);
        tvQuestion = (TextView) findViewById(R.id.tv_question);


        tvResponse = (TextView) findViewById(R.id.tv_response);
        b_restart = (Button) findViewById(R.id.b_restart);
        b_cheat = (Button) findViewById(R.id.cheat_button);
        b_skip = (Button) findViewById(R.id.skip_button);
        if(getIntent().getExtras().containsKey("question"))
            b_skip.setVisibility(View.INVISIBLE);
        b_highscores= (Button) findViewById(R.id.b_highscores);


        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        b_restart.setOnClickListener(this);



    }
    public void enableButtons(){
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        button4.setEnabled(true);
    }


    public void disableButtons(){
        button1.setEnabled(false);
        button2.setEnabled(false);
        button3.setEnabled(false);
        button4.setEnabled(false);
    }
}

