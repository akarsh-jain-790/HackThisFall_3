package com.shoparound.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shoparound.R;
import com.shoparound.utils.SignUpPrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUp2 extends AppCompatActivity {

    private EditText editTextKeyword, pincodeEt;
    private ChipGroup chipGroup;
    private Button buttonAdd, buttonShow, signupBtn;
    private ImageView backBtn;

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        this.editTextKeyword = (EditText) this.findViewById(R.id.interest_ET);
        this.chipGroup = (ChipGroup) this.findViewById(R.id.chipGroup);
        this.buttonAdd = (Button) this.findViewById(R.id.button_add);
        this.buttonShow = (Button) this.findViewById(R.id.button_show);
        this.backBtn = (ImageView) this.findViewById(R.id.backBtn);
        this.signupBtn = (Button) this.findViewById(R.id.sign_up);
        this.pincodeEt = (EditText) this.findViewById(R.id.pincode_ET);

        this.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewChip();
            }
        });
        this.buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSelections();
            }
        });

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

                String phone = fUser.getPhoneNumber();

                SignUpPrefManager pref = new SignUpPrefManager(SignUp2.this);

                Map<String, Object> map = new HashMap<>();
                map.put("Name", pref.getName());
                map.put("Email", pref.getEmail());
                map.put("Mobile", phone);
                map.put("Age", pref.getAge());
                map.put("Gender", pref.getGender());
                map.put("Pincode", pincodeEt.getText().toString().trim());
                map.put("Interest", showSelections());

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
        });

    }

    private void addNewChip() {
        String keyword = this.editTextKeyword.getText().toString();
        if (keyword == null || keyword.isEmpty()) {
            Toast.makeText(this, "Please enter the keyword!", Toast.LENGTH_LONG).show();
            return;
        }

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


            this.editTextKeyword.setText("");

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // User click on "Show Selections" button.
    private ArrayList<String> showSelections()  {
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