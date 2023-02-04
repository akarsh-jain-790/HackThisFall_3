package com.shoparound.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shoparound.R;

public class VerifyOTP extends AppCompatActivity {

    TextView otpTV;
    Button verifyBtn;
    ProgressBar pb;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


    String mVerificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        otpTV = findViewById(R.id.otp_ET);
        verifyBtn = findViewById(R.id.verify_button);
        pb = findViewById(R.id.progressbar);
        pb.setVisibility(View.GONE);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyBtn.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);
                if(otpTV.getText().toString().trim().length() != 6){
                    otpTV.setError("Invalid OTP Length");
                    verifyBtn.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.GONE);

                }else{
                    String code = otpTV.getText().toString().trim();
                    String phone = getIntent().getStringExtra("mobile");
                    mVerificationId = getIntent().getStringExtra("code");
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                    signInWithPhoneAuthCredential(credential, phone);
                }
            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, String phone) {
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI

                            fStore.collection("Users").document(phone).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot document = task.getResult();
                                        if(document.exists()){
                                            Intent i = new Intent(VerifyOTP.this, MainActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(i);
                                        }else{
                                            Intent i = new Intent(VerifyOTP.this, SignUp.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(i);
                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(VerifyOTP.this, "Something went wrong. Try Again!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(VerifyOTP.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            verifyBtn.setVisibility(View.VISIBLE);
                            pb.setVisibility(View.GONE);
                        }
                    }
                });
    }
}