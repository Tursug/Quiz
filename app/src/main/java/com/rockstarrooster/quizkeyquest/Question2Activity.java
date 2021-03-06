package com.rockstarrooster.quizkeyquest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Question2Activity extends AppCompatActivity {

    TextView question;
    TextView clock;
    EditText answer;
    Button nextButton;
    TextView result;

    private CountDownTimer answerCountDownTimer;
    private long answerTimeLeftInMilliseconds = 9000;

    private CountDownTimer resultCountdownTimer;
    private long resultTimeLeftInMilliseconds = 2000;

    String UserId;

    String Question1;
    String Answer1;

    int[] array;
    int[] UserPoint;

    String Question2;
    String Answer2;

    String userAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question2);

        //GET INTENT
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        //GET VARIABLES PASSED BY INTENT
        Question1 = extras.getString("QUESTION1");
        Answer1 = extras.getString("ANSWER1");
        UserId = extras.getString("USERID");
        UserPoint = extras.getIntArray("USERPOINT");
        array = extras.getIntArray("ARRAY");

        //GET AND SET QUESTİON
        question = findViewById(R.id.textView6);
        question.setText(Question1);

        //GET ANSWER
        answer = findViewById(R.id.editText5);

        //GET CLOCK
        clock = findViewById(R.id.textView);

        //GET RESULT
        result = findViewById(R.id.textView7);

        //START TIMER
        startTimer();

        //NEXT BUTTON
        nextButton = findViewById(R.id.button3);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAnswer=answer.getText().toString();
                stopTimer();
                checkResults();
            }
        });
    }

    //START THE TIMER
    public  void startTimer(){
        answerCountDownTimer = new CountDownTimer(answerTimeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                clock.setText(millisUntilFinished/1000+"");
            }

            @Override
            public void onFinish() {
                userAnswer=answer.getText().toString();
                stopTimer();
                checkResults();
            }
        }.start();
    }

    //STOP THE TIMER
    public void stopTimer(){
        answerCountDownTimer.cancel();
    }

    //START RESULT TIMER
    public void startResultTimer(){
        resultCountdownTimer = new CountDownTimer(resultTimeLeftInMilliseconds,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                stopResultTimer();
                next();
            }
        }.start();
    }

    //STOP RESULT TIMER
    public void stopResultTimer(){
        resultCountdownTimer.cancel();
    }

    //GO TO THE NEXT QUESTION
    public void next(){

        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Questions");
        databaseReference.child(Integer.toString(array[2])).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Question2 = dataSnapshot.child("Question").getValue().toString();
                Answer2 = dataSnapshot.child("Answer").getValue().toString();

                Intent intent = new Intent(Question2Activity.this, Question3Activity.class);
                Bundle extras = new Bundle();

                extras.putString("USERID", UserId);
                extras.putString("QUESTION1", Question2);
                extras.putString("ANSWER1", Answer2);
                extras.putIntArray("ARRAY", array);

                intent.putExtras(extras);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //CHECK THE RESULTS
    public void checkResults(){
        if (userAnswer.equals(Answer1)) {
            result.setText("TRUE!");

            MyAnswers myAnswers = (MyAnswers) getApplicationContext();
            int num = myAnswers.getNumber();
            myAnswers.setNumber(num+5);

        }else {
            result.setText(Answer1);
        }
        startResultTimer();
    }

    @Override
    public void onBackPressed(){
        return;
    }
}
