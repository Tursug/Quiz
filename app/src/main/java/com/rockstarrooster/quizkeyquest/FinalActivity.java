package com.rockstarrooster.quizkeyquest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FinalActivity extends AppCompatActivity {

    TextView yourScore;
    TextView firstScore;
    TextView secondScore;
    TextView thirdScore;
    //Button HomeButton;

    String UserId;
    int[] UserPoint;

    static int first_max = 0;
    static int second_max = 0;
    static int third_max = 0;

    static String first_name = "";
    static String second_name = "";
    static String third_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        //GET INTENT
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        //GET USERID AND POINT
        MyAnswers myAnswers = (MyAnswers) getApplicationContext();
        int total_point = myAnswers.getNumber();
        UserId = extras.getString("USERID");

        //GET TEXTVIEWS
        yourScore = findViewById(R.id.textView9);

        firstScore = findViewById(R.id.textView8);
        secondScore = findViewById(R.id.textView4);
        thirdScore = findViewById(R.id.textView3);

        //SET YOURSCORE
        yourScore.setText("Your Score: "+ total_point);

        //DATABASE REFERANCE
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //WRITE YOUR SCORE
        databaseReference.child("Users").child(UserId).child("Score").setValue(total_point);

        /*
        HomeButton = findViewById(R.id.button6);
        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAnswers myAnswers = (MyAnswers) getApplicationContext();
                myAnswers.setNumber(0);
                Intent intent = new Intent(FinalActivity.this, HomeActivity.class);
                intent.putExtra("USERID",UserId);
                startActivity(intent);
            }
        });
        */
        //READ TOP 3 SCORES AND THEIR USERNAME
        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                    int userScore = userSnap.child("Score").getValue(Integer.class);
                    String userName = userSnap.child("Username").getValue().toString();

                    if (userScore > first_max) {

                        third_max = second_max;
                        second_max = first_max;
                        first_max = userScore;

                        third_name = second_name;
                        second_name = first_name;
                        first_name = userName;

                    }else if(userScore < first_max && userScore > second_max){

                        third_max = second_max;
                        second_max = userScore;

                        third_name = second_name;
                        second_name = userName;

                    }else if(userScore < first_max && userScore < second_max && userScore > third_max){

                        third_max = userScore;
                        third_name = userName;
                    }
                }
                //PRINT HIGHSCORES
                firstScore.setText("1st "+first_name+": "+first_max);
                secondScore.setText("2nd "+second_name+": "+second_max);
                thirdScore.setText("3rd "+third_name+": "+third_max);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed(){
        return;
    }
}
