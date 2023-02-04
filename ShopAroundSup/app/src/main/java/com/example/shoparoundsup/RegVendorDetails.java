package com.example.shoparoundsup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shoparoundsup.util.RegPreferenceManager;

public class RegVendorDetails extends AppCompatActivity {

    Button nextBtn;
    ProgressBar pb;
    EditText nameEt, emailEt, ageEt;
    Spinner genderSp;
    ImageView profileIV;

    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_vendor_details);

        nextBtn = findViewById(R.id.nextBtn);
        pb = findViewById(R.id.progressbar);
        pb.setVisibility(View.GONE);

        nameEt = findViewById(R.id.nameET);
        emailEt = findViewById(R.id.emailEt);
        ageEt = findViewById(R.id.ageEt);
        profileIV = findViewById(R.id.user_image);

        genderSp = findViewById(R.id.genderSpinner);

        genderSp = findViewById(R.id.genderSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSp.setAdapter(adapter);

        profileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.GONE);

                String name = nameEt.getText().toString().trim();
                String email = emailEt.getText().toString().trim();
                String age = ageEt.getText().toString().trim();
                String gender = genderSp.getSelectedItem().toString().trim();

                if(imageUri == null) {
                    imageUri = Uri.parse("android.resource://" + RegVendorDetails.this.getPackageName() + "/drawable/avatar1");
                }
                Toast.makeText(RegVendorDetails.this, imageUri.toString(), Toast.LENGTH_SHORT).show();
                new RegPreferenceManager(RegVendorDetails.this).saveVendorDetails(name, email, gender, age, imageUri.toString());

                Intent i = new Intent(RegVendorDetails.this, RegShopDetails.class);
                startActivity(i);


            }
        });

        boolean checkIfUserDetailsExist = new RegPreferenceManager(RegVendorDetails.this).checkIfUserDetailsExist();
        if (!checkIfUserDetailsExist) {
            RegPreferenceManager prefManager = new RegPreferenceManager(RegVendorDetails.this);
            nameEt.setText(prefManager.getName());
            emailEt.setText(prefManager.getEmail());
            ageEt.setText("" + prefManager.getAge());
            int genderIndex = 0;
            if (prefManager.getGender().equals("Female")) genderIndex = 1;
            else if (prefManager.getGender().equals("Male")) genderIndex = 0;
            else genderIndex = 2;
            genderSp.setSelection(genderIndex);


        }
    }

    public static final int PICK_IMAGE = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            profileIV.setImageURI(imageUri);
        }
    }

}