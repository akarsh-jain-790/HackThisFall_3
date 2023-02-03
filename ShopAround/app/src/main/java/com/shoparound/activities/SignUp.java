package com.shoparound.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.shoparound.R;
import com.shoparound.utils.SignUpPrefManager;

public class SignUp extends AppCompatActivity {
    EditText name, email, age;
    Spinner gender;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        gender = (Spinner) findViewById(R.id.gender);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        gender.setAdapter(adapter);

        name = findViewById(R.id.name_ET);
        email = findViewById(R.id.email_ET);
        age = findViewById(R.id.age_ET);
        next = findViewById(R.id.sign_up_next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SignUpPrefManager(SignUp.this).saveUserDetails(
                        name.getText().toString().trim(),
                        email.getText().toString().trim(),
                        gender.getSelectedItem().toString().trim(),
                        Integer.parseInt(age.getText().toString().trim())
                );

                Intent i = new Intent(SignUp.this, SignUp2.class);
                startActivity(i);
            }
        });

        boolean checkIfUserDetailsExist = new SignUpPrefManager(SignUp.this).checkIfUserDetailsExist();
        if(!checkIfUserDetailsExist){
            SignUpPrefManager prefManager = new SignUpPrefManager(SignUp.this);
            name.setText(prefManager.getName());
            email.setText(prefManager.getEmail());
            age.setText("" + prefManager.getAge());
            int genderIndex = 0;
            if(prefManager.getGender().equals("Female")) genderIndex = 1;
            else if(prefManager.getGender().equals("Male")) genderIndex = 0;
            else genderIndex = 2;
            gender.setSelection(genderIndex);
        }
    }
}