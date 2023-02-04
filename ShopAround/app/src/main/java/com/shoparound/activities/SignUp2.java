package com.shoparound.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shoparound.R;
import com.shoparound.utils.SignUpPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import www.sanju.motiontoast.MotionToast;

public class SignUp2 extends AppCompatActivity {

    private EditText pincodeEt;
    private ChipGroup chipGroup;
    private Button signupBtn;
    private ImageView backBtn;

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    final StorageReference fileRef = storageReference.child("profile_images/"+ UUID.randomUUID().toString());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        this.chipGroup = (ChipGroup) this.findViewById(R.id.chipGroup);
        this.backBtn = (ImageView) this.findViewById(R.id.backBtn);
        this.signupBtn = (Button) this.findViewById(R.id.sign_up);
        this.pincodeEt = (EditText) this.findViewById(R.id.pincode_ET);

        ArrayList<String> categoryArray = new ArrayList<>();
        categoryArray.add("Clothes");
        categoryArray.add("Cosmetics");
        categoryArray.add("Kirana");
        categoryArray.add("Medical");
        categoryArray.add("Electronics");
        categoryArray.add("Hardware");

        for (String cat: categoryArray) {
            addNewChip(cat);
        }

        this.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUp2.this, SignUp.class);
                startActivity(i);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(validateUserDetails()){
//
//                }
                SignUpPrefManager prefManager = new SignUpPrefManager(SignUp2.this);

                UploadTask uploadTask = fileRef.putFile(Uri.parse(prefManager.getProfileImage()));
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Uri url = uri;

                                String phone = fUser.getPhoneNumber();

                                SignUpPrefManager pref = new SignUpPrefManager(SignUp2.this);

                                Map<String, Object> map = new HashMap<>();
                                map.put("Name", pref.getName());
                                map.put("Email", pref.getEmail());
                                map.put("Mobile", phone);
                                map.put("Age", pref.getAge());
                                map.put("Gender", pref.getGender());
                                map.put("Pincode", pincodeEt.getText().toString().trim());
                                map.put("Interest", getSelections());
                                map.put("ProfileImage", String.valueOf(url));

                                fStore.collection("Users").document(phone).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(SignUp2.this, "User Created Successfully!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(SignUp2.this, MainActivity.class);
                                        startActivity(i);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignUp2.this, "Error while creating user. Try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });
                            }
                        });
                    }
                });
    }

    private void validateUserDetails() {
        boolean pincodeValidation = false;
        String URL = "https://api.apyhub.com/validate/postcodes/in";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("postcode", pincodeEt.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("data"))
                                MotionToast.Companion.darkColorToast(SignUp2.this,
                                        "Error",
                                        "Invalid Pincode.",
                                        MotionToast.TOAST_WARNING,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(SignUp2.this, R.font.regular));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MotionToast.Companion.darkColorToast(SignUp2.this,
                                "Error",
                                "Unable to verify email.",
                                MotionToast.TOAST_WARNING,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(SignUp2.this, R.font.regular));
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("apy-token", "APT0ebC2WhzBrg0QP2jVPTgn4FR554cJvSyiKy8hCBrydCgtY0Z0mjyFoQ");
                return headers;
            }
        };

        // Add the request to the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(SignUp2.this);
        queue.add(jsonObjectRequest);
    }

    private void addNewChip(String keyword) {
        try {
            LayoutInflater inflater = LayoutInflater.from(this);

            // Create a Chip from Layout.
            Chip newChip = (Chip) inflater.inflate(R.layout.chip, this.chipGroup, false);
            newChip.setText(keyword);
            this.chipGroup.addView(newChip);

            // Set Listener for the Chip:
            newChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    handleChipCheckChanged((Chip) buttonView, isChecked);
                }
            });

            newChip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleChipCloseIconClicked((Chip) v);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            MotionToast.Companion.darkColorToast(SignUp2.this,
                    "Error",
                    "Error while adding new Chip.",
                    MotionToast.TOAST_WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(SignUp2.this, R.font.regular));
        }
    }

    // User click on "Show Selections" button.
    private ArrayList<String> getSelections()  {
        int count = this.chipGroup.getChildCount();

        ArrayList<String> list = new ArrayList<>();

        String s = null;
        for(int i=0;i< count; i++) {
            Chip child = (Chip) this.chipGroup.getChildAt(i);

            if(!child.isChecked()) {
                continue;
            }

            list.add(child.getText().toString());
        }
        return list;
    }

    // User close a Chip.
    private void handleChipCloseIconClicked(Chip chip) {
        ChipGroup parent = (ChipGroup) chip.getParent();
        parent.removeView(chip);
    }

    // Chip Checked Changed
    private void handleChipCheckChanged(Chip chip, boolean isChecked) {
    }
}