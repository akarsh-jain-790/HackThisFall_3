package com.shoparound.activities;

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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shoparound.R;
import com.shoparound.utils.SignUpPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import www.sanju.motiontoast.MotionToast;

public class SignUp extends AppCompatActivity {
    EditText name, email, age;
    Spinner gender;
    Button next;

    Uri imageUri;
    ImageView image_preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(!isConnected){
            MotionToast.Companion.darkColorToast(SignUp.this,
                    "No Internet Connection!",
                    "Please check your Internet connection and try again.",
                    MotionToast.TOAST_NO_INTERNET,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(SignUp.this,R.font.regular));
        }else{
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
            image_preview = findViewById(R.id.user_image);

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isValidSignUpDetails()){
                        // Progress Bar
                        loading(true);

                        SignUpPrefManager prefManager = new SignUpPrefManager(SignUp.this);
                        prefManager.saveUserDetails(
                                name.getText().toString().trim(),
                                email.getText().toString().trim(),
                                gender.getSelectedItem().toString().trim(),
                                imageUri.toString(),
                                Integer.parseInt(age.getText().toString().trim())
                        );
                        Intent i = new Intent(SignUp.this, SignUp2.class);
                        startActivity(i);
                        loading(false);
                    }
                }
            });

            boolean checkIfUserDetailsExist = new SignUpPrefManager(SignUp.this).checkIfUserDetailsExist();

            if(!checkIfUserDetailsExist){
                SignUpPrefManager prefManager = new SignUpPrefManager(SignUp.this);

                name.setText(prefManager.getName());
                email.setText(prefManager.getEmail());
                age.setText("" + prefManager.getAge());
//                image_preview.setImageURI(prefManager.getProfileImage());

                int genderIndex = 0;
                if(prefManager.getGender().equals("Female")) genderIndex = 1;
                else if(prefManager.getGender().equals("Male")) genderIndex = 0;
                else genderIndex = 2;
                gender.setSelection(genderIndex);
            }

            // image
            image_preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                }
            });
        }
    }
    // Validate Function
    private Boolean isValidSignUpDetails(){
        name = findViewById(R.id.name_ET);
        email = findViewById(R.id.email_ET);
        age = findViewById(R.id.age_ET);

        String URL = "https://api.apyhub.com/validate/email/dns";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);

                            if (!json.getBoolean("data")){
                                MotionToast.Companion.darkColorToast(SignUp.this,
                                        "Error",
                                        "Invalid Email address!",
                                        MotionToast.TOAST_WARNING,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(SignUp.this, R.font.regular));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MotionToast.Companion.darkColorToast(SignUp.this,
                                "Error",
                                "Unable to verify email.",
                                MotionToast.TOAST_WARNING,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(SignUp.this, R.font.regular));
                        }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("apy-token", "APT0ebC2WhzBrg0QP2jVPTgn4FR554cJvSyiKy8hCBrydCgtY0Z0mjyFoQ");
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return ("{\"email\":\"" + email.getText().toString() + "\"}").getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            }
        };

        // Add the request to the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(SignUp.this);
        queue.add(stringRequest);

        if (name.getText().toString().trim().length() == 0) {
            MotionToast.Companion.darkColorToast(SignUp.this,
                    "Error",
                    "Username can't be empty.",
                    MotionToast.TOAST_WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(SignUp.this, R.font.regular));
            return false;
        }else if (email.getText().toString().trim().length() == 0) {
            MotionToast.Companion.darkColorToast(SignUp.this,
                    "Error",
                    "Email can't be empty.",
                    MotionToast.TOAST_WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(SignUp.this, R.font.regular));
            return false;
        }else if (age.getText().toString().trim().length() == 0) {
            MotionToast.Companion.darkColorToast(SignUp.this,
                    "Error",
                    "Age can't be empty.",
                    MotionToast.TOAST_WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(SignUp.this, R.font.regular));
            return false;
        }else if (imageUri == null) {
            imageUri = Uri.parse("android.resource://"+this.getPackageName()+"/drawable/avatar");
            return true;
        }else{
            MotionToast.Companion.darkColorToast(SignUp.this,
                    "Error",
                    "Invalid Email address!",
                    MotionToast.TOAST_WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(SignUp.this, R.font.regular));
            return false;
        }
    }
    // Loading
    private void loading(Boolean isLoading){
        if(isLoading){
            next = findViewById(R.id.sign_up_next);
            next.setVisibility(View.INVISIBLE);
            ProgressBar progressBar = findViewById(R.id.progressbar);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            next = findViewById(R.id.sign_up_next);
            next.setVisibility(View.VISIBLE);
            ProgressBar progressBar = findViewById(R.id.progressbar);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    //    Image Picker
    public static final int PICK_IMAGE = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            image_preview.setImageURI(imageUri);
        }
    }
}