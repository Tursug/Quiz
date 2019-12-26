package com.rockstarrooster.quizkeyquest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    EditText emailId, password, username;
    Button btnRegister;
    TextView tvSingIn;

    FirebaseAuth mFirebaseAuth;

    public static final String intentUserID = "USERID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //FULLSCREEN
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FIND VIEWS
        emailId = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        btnRegister = findViewById(R.id.button);
        tvSingIn = findViewById(R.id.textView2);
        username = findViewById(R.id.editText3);
        mFirebaseAuth = FirebaseAuth.getInstance();

        // REGISTER
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //GET EMAIL AND PASSWORD
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                final String usn = username.getText().toString();

                //CREATE USER WITH EMAIL AND PASSWORD
                if (email.isEmpty() || pwd.isEmpty() || usn.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }else {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                //GET USERID
                                String userId = mFirebaseAuth.getCurrentUser().getUid();

                                //DATABASE REFERANCE
                                DatabaseReference databaseReference;
                                databaseReference = FirebaseDatabase.getInstance().getReference();

                                // ADD TO THE DATABASE
                                databaseReference.child("Users").child(userId).child("Score").setValue(0);
                                databaseReference.child("Users").child(userId).child("Username").setValue(usn);

                                // GO TO THE HOME, PASS USERID,
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                intent.putExtra(intentUserID, userId);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });

        //GO TO THE LOGIN PAGE
        tvSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed(){
        return;
    }
}
