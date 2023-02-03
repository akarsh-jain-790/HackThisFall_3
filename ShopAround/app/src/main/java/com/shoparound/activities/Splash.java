package com.shoparound.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shoparound.R;
import com.shoparound.utils.SignUpPrefManager;

public class Splash extends AppCompatActivity {

    FirebaseUser fUser;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                if(fUser != null){
                    alreadyRegisteredCheck(fUser);
                }else{
                    Intent i = new Intent(Splash.this, Login.class);
                    startActivity(i);
                }
            }
        }, 3000);
    }

    private void alreadyRegisteredCheck(FirebaseUser fUser) {
        String mobile = fUser.getPhoneNumber();
        fStore.collection("Users").document(mobile).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        System.out.println("yupp");
                        Intent i = new Intent(Splash.this, MainActivity.class);
                        startActivity(i);
                    }else{
                        if(new SignUpPrefManager(Splash.this).checkIfUserDetailsExist()){
                            Intent i = new Intent(Splash.this, SignUp.class);
                            startActivity(i);
                        }else{
                            Intent i = new Intent(Splash.this, SignUp2.class);
                            startActivity(i);
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Splash.this, "Something went wrong. Try Again!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}