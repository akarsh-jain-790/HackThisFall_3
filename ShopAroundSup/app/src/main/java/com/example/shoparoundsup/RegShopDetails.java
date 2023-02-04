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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shoparoundsup.util.RegPreferenceManager;
import com.example.shoparoundsup.util.RegShopPreferenceManager;

public class RegShopDetails extends AppCompatActivity {

    ImageView backBtn;
    Button nextBtn;

    EditText shopNameEt, addressEt, cityEt, pincodeEt;
    Spinner categorySpinner;
    ImageView shopImage;
    Uri shopImgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_shop_details);

        backBtn = findViewById(R.id.backImg);
        nextBtn = findViewById(R.id.nextBtn);

        shopNameEt = findViewById(R.id.shopNameTV);
        addressEt = findViewById(R.id.address1TV);
        cityEt = findViewById(R.id.cityTv);
        pincodeEt = findViewById(R.id.pincodeTV);
        categorySpinner = findViewById(R.id.categoriesSpinner);
        shopImage = findViewById(R.id.shop_Image);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);


        shopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegShopDetails.this, RegVendorDetails.class);
                startActivity(i);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String shopName = shopNameEt.getText().toString().trim();
                String address = addressEt.getText().toString().trim();
                String city = cityEt.getText().toString().trim();
                String pincode = pincodeEt.getText().toString().trim();
                String category = categorySpinner.getSelectedItem().toString().trim();

                if(shopImgUri == null) {
                    shopImgUri = Uri.parse("android.resource://" + RegShopDetails.this.getPackageName() + "/drawable/avatar1");
                }

                Toast.makeText(RegShopDetails.this, shopImgUri.toString(), Toast.LENGTH_SHORT).show();
                new RegShopPreferenceManager(RegShopDetails.this).saveShopDetails(shopName, address, city, pincode, category, shopImgUri.toString());

                Intent i = new Intent(RegShopDetails.this, KYC.class);
                startActivity(i);
            }
        });

        boolean checkIfUserDetailsExist = new RegShopPreferenceManager(RegShopDetails.this).checkIfUserDetailsExist();
        if (!checkIfUserDetailsExist) {
            RegShopPreferenceManager prefManager = new RegShopPreferenceManager(RegShopDetails.this);
            shopNameEt.setText(prefManager.getshopName());
            addressEt.setText(prefManager.getaddress());
            cityEt.setText(prefManager.getcity());
            pincodeEt.setText(prefManager.getPincode());
        }

    }

    public static final int PICK_IMAGE = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            shopImgUri = data.getData();
            shopImage.setImageURI(shopImgUri);
        }
    }

}