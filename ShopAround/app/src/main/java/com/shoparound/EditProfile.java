package com.shoparound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shoparound.activities.MainActivity;
import com.shoparound.activities.SignUp;
import com.shoparound.activities.SignUp2;

import java.util.HashMap;
import java.util.Map;

import www.sanju.motiontoast.MotionToast;

public class EditProfile extends AppCompatActivity {
    private FirebaseFirestore db;
    EditText name, email, age;
    Spinner gender;
    Button save;

    Uri imageUri;
    ImageView image_preview;

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(!isConnected){
            MotionToast.Companion.darkColorToast(EditProfile.this,
                    "No Internet Connection!",
                    "Please check your Internet connection and try again.",
                    MotionToast.TOAST_NO_INTERNET,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(EditProfile.this,R.font.regular));
        }else {
            gender = (Spinner) findViewById(R.id.gender);
            name = findViewById(R.id.name_ET);
            email = findViewById(R.id.email_ET);
            age = findViewById(R.id.age_ET);
            save = findViewById(R.id.save_data);
            image_preview = findViewById(R.id.user_image);

            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.gender_array, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            gender.setAdapter(adapter);

            db = FirebaseFirestore.getInstance();
            FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
            db.collection("Users").document(fUser.getPhoneNumber()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    name.setText(documentSnapshot.getString("Name"));
                    email.setText(documentSnapshot.getString("Email"));
                    int genderIndex = 0;
                    if(documentSnapshot.getString("Gender").equals("Female")) genderIndex = 1;
                    else if(documentSnapshot.getString("Gender").equals("Male")) genderIndex = 0;
                    else genderIndex = 2;
                    gender.setSelection(genderIndex);
                    age.setText(""+documentSnapshot.getLong("Age"));
                }
            });

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("Name", name.getText().toString().trim());
                    map.put("Email", email.getText().toString().trim());
                    map.put("Age", Integer.parseInt(age.getText().toString().trim()));
                    map.put("Gender", gender.getSelectedItem().toString().trim());

                    String phone = fUser.getPhoneNumber();

                    fStore.collection("Users").document(phone).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditProfile.this, "User Updated Successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfile.this, "Error while Updating user. Try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}