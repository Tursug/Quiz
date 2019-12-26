package com.rockstarrooster.quizkeyquest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {

    Button logOut;
    Button Start;
    TextView ID;

    //INTENT DATA
    String UserID;
    int UserPoint = 0;

    String Question1;
    String Answer1;

    String Question2;
    String Answer2;

    String Question3;
    String Answer3;

    FirebaseAuth mFirebaseAuth;

    public static final String intentUserID = "USERID";

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle extras = getIntent().getExtras();

        UserID = extras.getString(intentUserID);

        /**/
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        databaseReference.child(UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String strusrName = dataSnapshot.child("Username").getValue().toString();
                ID.setText("ID:"+" "+strusrName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /**/
        ID = findViewById(R.id.textView5);
        //ID.setText("ID:"+" "+UserID);

        logOut = findViewById(R.id.button2);
        Start = findViewById(R.id.button4);

        //LOGOUT
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });

        //START THE GAME
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v){
                final int[] UserPoint = {0, 0, 0, 0};
                final int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9};
                Random rand = new Random();

                for(int i=0;i<array.length;i++) {
                    int randomIndexSwap = rand.nextInt(array.length);
                    int temp = array[randomIndexSwap];
                    array[randomIndexSwap] = array[i];
                    array[i] = temp;
                }

                DatabaseReference databaseReference;
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Questions");

                databaseReference.child(Integer.toString(array[0])).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Question1 = dataSnapshot.child("Question").getValue().toString();
                        Answer1 = dataSnapshot.child("Answer").getValue().toString();

                        Intent intent = new Intent(HomeActivity.this, Question1Activity.class);
                        Bundle extras = new Bundle();

                        extras.putString("USERID", UserID);
                        extras.putIntArray("USERPOINT", UserPoint);
                        extras.putString("QUESTION1", Question1);
                        extras.putString("ANSWER1", Answer1);
                        extras.putIntArray("ARRAY",array);

                        intent.putExtras(extras);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
    @Override
    public void onBackPressed(){
        return;
    }
}
